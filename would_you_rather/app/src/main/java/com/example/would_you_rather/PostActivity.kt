package com.example.would_you_rather

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PostActivity : AppCompatActivity() {

    private val recordAudioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchVoiceInput()
        } else {
            Toast.makeText(this, "Microphone permission is needed for voice input.", Toast.LENGTH_SHORT).show()
        }
    }

    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            val questionInput = findViewById<EditText>(R.id.input_question)
            questionInput.setText(spokenText ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val username = intent.getStringExtra("username") ?: ""

        val questionInput = findViewById<EditText>(R.id.input_question)
        val option1Input = findViewById<EditText>(R.id.input_option1)
        val option2Input = findViewById<EditText>(R.id.input_option2)
        val ratingBar = findViewById<RatingBar>(R.id.questionRating)
        val ratingLabel = findViewById<TextView>(R.id.ratingLabel)
        val submitButton = findViewById<Button>(R.id.submitPost)
        val voiceButton = findViewById<Button>(R.id.button_voice)
        val navigationView = findViewById<com.example.would_you_rather.NavigationView>(R.id.navigation)

        ratingBar.rating = LocalPrefs.getLastRating(this)
        ratingLabel.text = "How fun is this question? (${ratingBar.rating.toInt()}/5)"

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            LocalPrefs.saveLastRating(this, rating)
            ratingLabel.text = "How fun is this question? (${rating.toInt()}/5)"
        }

        voiceButton.setOnClickListener { ensureMicrophonePermissionThenLaunch() }

        submitButton.setOnClickListener {
            val question = questionInput.text.toString()
            val option1 = option1Input.text.toString()
            val option2 = option2Input.text.toString()

            if (question.isBlank() || option1.isBlank() || option2.isBlank()) {
                Toast.makeText(this, "Please fill in the question and both options.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Backend.post(
                question,
                option1,
                option2,
                onSuccess = {
                    Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { message ->
                    Toast.makeText(this, "Unable to post: $message", Toast.LENGTH_SHORT).show()
                }
            )
        }

        navigationView.setOnHomeClick {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        navigationView.setOnPostClick { /* already here */ }
    }

    private fun ensureMicrophonePermissionThenLaunch() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (granted) {
            launchVoiceInput()
        } else {
            recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun launchVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your question")
        }
        voiceLauncher.launch(intent)
    }
}
