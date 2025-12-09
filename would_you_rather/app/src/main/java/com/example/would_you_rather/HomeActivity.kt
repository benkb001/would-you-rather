package com.example.would_you_rather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

/*
    TODO: We will need to make a view for the
    user to scroll through posts. We will need
    to call Backend.getPost(username) to
    get information about the next post we
    want the user to see, then build a
    view using WouldYouRatherView and display it.
    You can get the username with intent.getStringExtra("username")
    We'll also need a button somewhere to create a new post (it should open PostActivity via an intent)
*/
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("username") ?: return

        val postContainer = findViewById<LinearLayout>(R.id.postContainer)
        val createPostButton = findViewById<Button>(R.id.createPostButton)

        // loads async
        Backend.getPost(
            onSuccess = { post ->
                val postView = WouldYouRatherView(this)
                postView.setPost(post, username)  // added a username parameter
                postContainer.addView(postView)
            },
            onError = { message ->
                // Handle no posts or error
            }
        )


        // TODO: Load a new post when an option is clicked

        // Create new post button -> open PostActivity
        createPostButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

    }

}