package com.example.would_you_rather

import android.content.Intent
import android.os.Bundle
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


        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            val user : String = findViewById<EditText>(R.id.input_username).text.toString()
            val password : String = findViewById<EditText>(R.id.input_password).text.toString()
            try {
                Backend.signIn(user, password)
                val intent : Intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("username", user)
                startActivity(intent)
            } catch (e : Exception) {
                val root : ConstraintLayout = findViewById<ConstraintLayout>(R.id.main)
                Snackbar.make(
                    root,
                    "Unable to sign in: ${e.message}",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}