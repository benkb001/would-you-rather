package com.example.would_you_rather

import org.json.JSONObject

class Backend {
    companion object {
        // TODO: we need to set up firebase so that this line will work
        // var db : FirebaseDatabase = FirebaseDatabase.getInstance()

        fun signUp(username : String, password : String, password_confirmation : String) : Unit {
            /*
            TODO: This function will look into the firebase server and check if
            there is an error. Possible errors:
            a user with the provided username already exists
            the password and password_confirmation do not match

            If there are no errors, it will add the user to the database
            There will be a 'users' array under the root,
            each user element will be a json
            with various information, for now just assign their username,
            password, email.
             */
        }

        fun signIn(username : String, password : String) : Unit {
            /*
            TODO: This function will attempt to sign the user in
            and throw an error if either the user does not exist,
            or the password provided doesn't match the one we
            have in the database
             */
        }

        fun getPost(username : String) : Post {
            TODO()
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

        }

        fun choose(option : Int, post_id : String, username : String) : JSONObject {
            TODO()
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
        }

        fun post(question : String, option1 : String, option2 : String, author : String) : Unit {
            TODO()
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
        }
    }
}
