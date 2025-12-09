package com.example.would_you_rather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // DEBUGGING TESTING:
        Log.d("FIREBASE_TEST", "calling signUp...")

        Backend.signUp("testuser2", "password123", "password123",
            onSuccess = {
                Log.d("FIREBASE_TEST", "yay works! usernames/users on console")
            },
            onError = { message ->
                Log.d("FIREBASE_TEST", "error: $message")
            }
        )

        Log.d("FIREBASE_TEST", "signUp called, wait for callback!")



        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        findViewById<Button>(R.id.button_sign_up).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            val user : String = findViewById<EditText>(R.id.input_username).text.toString()
            val password : String = findViewById<EditText>(R.id.input_password).text.toString()
            Backend.signIn(user, password,
                onSuccess = { username ->
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                },
                onError = { message ->
                    val root = findViewById<ConstraintLayout>(R.id.main)
                    Snackbar.make(root, "unable to sign in: $message", Snackbar.LENGTH_SHORT).show()
                }
            )
        }



    }
}