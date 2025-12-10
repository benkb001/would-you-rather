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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

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
    private var postView: WouldYouRatherView? = null
    private var adView: AdView? = null
    private lateinit var postContent: LinearLayout
    private lateinit var statusMessage: TextView
    private lateinit var textSizeLabel: TextView
    private lateinit var textSizeSeekBar: SeekBar
    private var currentUsername: String = ""
    private var hasLoadedPost = false
    private var shouldRefreshOnResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("username") ?: run {
            finish()
            return
        }
        currentUsername = username

        postContent = findViewById(R.id.post_content)
        val createPostButton = findViewById<Button>(R.id.createPostButton)
        val navigationView = findViewById<com.example.would_you_rather.NavigationView>(R.id.navigation)
        textSizeLabel = findViewById(R.id.textSizeLabel)
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar)
        statusMessage = findViewById(R.id.statusMessage)
        val adContainer = findViewById<LinearLayout>(R.id.ad_view)

        optionTextSizeSp = LocalPrefs.getOptionTextSize(this)
        textSizeSeekBar.progress = optionTextSizeSp
        textSizeLabel.text = "Option text size (${optionTextSizeSp}sp)"

        MobileAds.initialize(this)
        adView = AdView(this).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = "ca-app-pub-3940256099942544/6300978111" // Google-provided test banner unit
        }
        adContainer.removeAllViews()
        adContainer.addView(adView)
        adView?.loadAd(
            AdRequest.Builder()
                .addKeyword("fun")
                .addKeyword("polls")
                .build()
        )

        loadPost()

        // Create new post button -> open PostActivity
        createPostButton.setOnClickListener {
            shouldRefreshOnResume = true
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Navigation bar handling
        navigationView.setOnHomeClick { /* already here */ }
        navigationView.setOnPostClick {
            shouldRefreshOnResume = true
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
                postView?.setOptionTextSize(optionTextSizeSp)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun loadPost() {
        if (currentUsername.isBlank()) return

        statusMessage.visibility = View.VISIBLE
        statusMessage.text = "Loading posts..."
        Backend.getPost(
            onSuccess = { post ->
                statusMessage.visibility = View.GONE
                postContent.removeAllViews()

                postView = WouldYouRatherView(this)
                postView?.setCurrentUser(currentUsername)
                postView?.setOptionTextSize(optionTextSizeSp)
                postView?.setPost(post, currentUsername)
                postView?.setOnVoteComplete { loadPost() }
                postContent.addView(postView)
            },
            onError = { message ->
                statusMessage.visibility = View.VISIBLE
                statusMessage.text = "Unable to load posts: $message"
            }
        )
        hasLoadedPost = true
    }

    override fun onPause() {
        shouldRefreshOnResume = true
        adView?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
        if (shouldRefreshOnResume || !hasLoadedPost) {
            loadPost()
            shouldRefreshOnResume = false
        }
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }
}
