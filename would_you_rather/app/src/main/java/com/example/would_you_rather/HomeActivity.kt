package com.example.would_you_rather

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.View

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
    private var optionTextSizeSp: Int = 18

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("username") ?: return

        val postContainer = findViewById<LinearLayout>(R.id.postContainer)
        val createPostButton = findViewById<Button>(R.id.createPostButton)
        val navigationView = findViewById<com.example.would_you_rather.NavigationView>(R.id.navigation)
        val textSizeLabel = findViewById<TextView>(R.id.textSizeLabel)
        val textSizeSeekBar = findViewById<SeekBar>(R.id.textSizeSeekBar)
        val statusMessage = findViewById<TextView>(R.id.statusMessage)

        optionTextSizeSp = LocalPrefs.getOptionTextSize(this)
        textSizeSeekBar.progress = optionTextSizeSp
        textSizeLabel.text = "Option text size (${optionTextSizeSp}sp)"

        // Load a post with backend; fallback to sample while Backend is stubbed
        val post = try {
            val p = Backend.getPost(username)
            statusMessage.visibility = View.GONE
            p
        } catch (e: Throwable) {
            statusMessage.visibility = View.VISIBLE
            statusMessage.text = "Showing sample while backend is pending."
            Post(
                post_id = "sample1",
                question = "Travel to the past or the future?",
                option1 = "Past",
                option2 = "Future",
                option1Count = 12,
                option2Count = 18,
                author = "demo_user"
            )
        }
        val postView = WouldYouRatherView(this)
        postView.setCurrentUser(username)
        postView.setOptionTextSize(optionTextSizeSp)
        postView.setPost(post)
        postContainer.addView(postView)

        // TODO: Once Backend.choose/getPost are implemented, load the next post after a vote. (Waiting on Saanvi)

        // Create new post button -> open PostActivity
        createPostButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Navigation bar handling
        navigationView.setOnHomeClick { /* already here */ }
        navigationView.setOnPostClick {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Text size adjustment and persistence
        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                optionTextSizeSp = progress.coerceAtLeast(12)
                textSizeLabel.text = "Option text size (${optionTextSizeSp}sp)"
                LocalPrefs.saveOptionTextSize(this@HomeActivity, optionTextSizeSp)
                postView.setOptionTextSize(optionTextSizeSp)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

}
