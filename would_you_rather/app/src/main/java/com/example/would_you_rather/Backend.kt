package com.example.would_you_rather

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class Backend {
    companion object {
        private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun signUp(
                      username: String,
                      password: String,
                      passwordConfirmation: String,
                      onSuccess: () -> Unit,
                      onError: (String) -> Unit) {
            /* TODO: This function will look into the firebase server and check if
            there is an error. Possible errors:
            a user with the provided username already exists
            the password and password_confirmation do not match

            If there are no errors, it will add the user to the database
            There will be a 'users' array under the root,
            each user element will be a json
            with various information, for now just assign their username,
            password, email.
             */
            if (password != passwordConfirmation) {
                onError("Passwords do not match.")
                return
            }

            val email = "$username@wouldyourather.app"

            db.getReference("usernames").child(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            onError("Username already taken")
                            return
                        }

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = auth.currentUser?.uid
                                    if (uid == null) {
                                        onError("Unable to create account; please try again.")
                                        return@addOnCompleteListener
                                    }

                                    val userData = mapOf(
                                        "username" to username,
                                        "email" to email,
                                        "seen_posts" to mapOf<String, Boolean>()
                                    )
                                    db.getReference("users").child(uid).setValue(userData)
                                    db.getReference("usernames").child(username).setValue(email)

                                    onSuccess()
                                } else {
                                    onError(task.exception?.message ?: "Sorry, sign up failed")
                                }
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError(error.message)
                    }
                })



                }


        fun signIn(
            username: String,
            password: String,
            onSuccess: (username: String) -> Unit,
            onError: (String) -> Unit) {
            /*
            TODO: This function will attempt to sign the user in
            and throw an error if either the user does not exist,
            or the password provided doesn't match the one we
            have in the database
             */
            db.getReference("usernames").child(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = snapshot.getValue(String::class.java)
                    if (email == null) {
                        onError("Sorry, user does not exist")
                        return
                    }

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSuccess(username)
                            } else {
                                onError(task.exception?.message ?: "password does not match")
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })

        }

        fun signOut() {
            auth.signOut()
        }

        fun getCurrentUser(): String? {
            return auth.currentUser?.uid
        }

        fun getPost(
            onSuccess: (Post) -> Unit,
            onError: (String) -> Unit
        ) {


            /*
            This function will get a post the user
            hasn't yet seen and return some json, including
            the question, each answer, and the current percentages
            and absolute quantities of each response.
            The root of the database will have a list of posts.
            Each post will have an id. Each user
            will have an array of postId strings called seen_posts representing
            the posts they have already seen. Just return
            the next post with an id that is not in the user's
            seen_posts
             */
            val uid = auth.currentUser?.uid
            if (uid == null) {
                onError("user not logged in")
                return
            }

            db.getReference("users").child(uid).child("seen_posts")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(seenSnapshot: DataSnapshot) {
                        val seenPosts = seenSnapshot.children.mapNotNull { it.key }.toSet()

                        db.getReference("posts")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(postsSnapshot: DataSnapshot) {
                                    for (postSnapshot in postsSnapshot.children) {
                                        val postId = postSnapshot.key ?: continue
                                        if (postId !in seenPosts) {
                                            val post = postSnapshot.getValue(Post::class.java)
                                            if (post != null) {
                                                onSuccess(post)
                                                return
                                            }
                                        }
                                    }
                                    onError("no new posts are available")
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    onError(error.message)
                                }
                            })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError(error.message)
                    }
                })

        }

        fun choose(
            option: Int,
            postId: String,
            onSuccess: (option1Count: Int, option2Count: Int) -> Unit,
            onError: (String) -> Unit
        ) {
            /*
            This function is used to update the counts of the associated
            post and return the new counts, and also tell us that the user
            has already seen this post.

            Firstly, it will find the post with post_id and increment
            either option 0 or option 1's total count of people that have selected it.
            Then it will find the user with username and append post_id to their
            seen_posts.

            This option will throw an error if an int other than 0 or 1 is passed,
            if a user with username doesn't exist, or if a post with post_id doesn't exist
             */
            if (option !in 0..1) {
                onError("Option must be 0 or 1")
                return
            }

            val uid = auth.currentUser?.uid
            if (uid == null) {
                onError("User not logged in")
                return
            }

            val userSeenRef = db.getReference("users").child(uid)
                .child("seen_posts").child(postId)
            val postRef = db.getReference("posts").child(postId)
            val countField = if (option == 0) "option1Count" else "option2Count"

            // First, claim the vote for this user to prevent double votes.
            userSeenRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val alreadyVoted = currentData.getValue(Boolean::class.java) ?: false
                    if (alreadyVoted) {
                        return Transaction.abort()
                    }
                    currentData.value = true
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    snapshot: DataSnapshot?
                ) {
                    if (error != null) {
                        onError(error.message)
                        return
                    }
                    if (!committed) {
                        onError("You already voted on this post.")
                        return
                    }

                    // Now increment the chosen option count.
                    postRef.child(countField)
                        .runTransaction(object : Transaction.Handler {
                            override fun doTransaction(currentData: MutableData): Transaction.Result {
                                val currentCount = currentData.getValue(Int::class.java) ?: 0
                                currentData.value = currentCount + 1
                                return Transaction.success(currentData)
                            }

                            override fun onComplete(
                                error: DatabaseError?,
                                committed: Boolean,
                                snapshot: DataSnapshot?
                            ) {
                                if (error != null) {
                                    // Roll back the seen flag so user can retry if increment fails.
                                    userSeenRef.setValue(null)
                                    onError(error.message)
                                    return
                                }
                                if (!committed) {
                                    userSeenRef.setValue(null)
                                    onError("Failed to record vote, please try again.")
                                    return
                                }

                                postRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(postSnapshot: DataSnapshot) {
                                        val opt1 = postSnapshot.child("option1Count").getValue(Int::class.java) ?: 0
                                        val opt2 = postSnapshot.child("option2Count").getValue(Int::class.java) ?: 0
                                        onSuccess(opt1, opt2)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        onError(error.message)
                                    }
                                })
                            }
                        })
                }
            })


        }

        fun post(
            question: String,
            option1: String,
            option2: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {

            /*
            This function will generate a new post. It will make a new unique post_id,
            and make an object like this:
            {
                post_id : "abc123def456"
                question : "Red or Blue?"
                option1 : "Red"
                option2 : "blue"
                option1Count : 0
                option2Count : 0
                author : "herve123"
            }
            and append it to posts
             */
            val uid = auth.currentUser?.uid
            if (uid == null) {
                onError("User not logged in")
                return
            }

            db.getReference("users").child(uid).child("username")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val username = snapshot.getValue(String::class.java) ?: "Anonymous"

                        val postId = UUID.randomUUID().toString()
                        val newPost = Post(
                            post_id = postId,
                            question = question,
                            option1 = option1,
                            option2 = option2,
                            option1Count = 0,
                            option2Count = 0,
                            author = username
                        )

                        db.getReference("posts").child(postId).setValue(newPost)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e -> onError(e.message ?: "failed to create post") }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onError(error.message)
                    }
                })


        }
    }
}
