package com.example.would_you_rather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.button_sign_up).setOnClickListener {
            val user : String = findViewById<EditText>(
                R.id.input_username).text.toString()
            val password = findViewById<EditText>(
                R.id.input_password).text.toString()
            val password_confirmed = findViewById<EditText>(
                R.id.input_password_confirm).text.toString()

            Backend.signUp(user, password, password_confirmed,
                onSuccess = {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("username", user)
                    startActivity(intent)
                },
                onError = { message ->
                    val root = findViewById<ConstraintLayout>(R.id.main)
                    Snackbar.make(root, "Unable to sign up: $message", Snackbar.LENGTH_SHORT).show()
                }
            )
        }

        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}