package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.R
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.ActivityLoginBinding
import com.gokmenmutlu.exampleinstagramclonekotlinfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null) { // giriş yapılı ise direkt mainActivity e git.
            goToMainActivity()
        }
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@LoginActivity,"Please fill in all fields!", Toast.LENGTH_SHORT).show()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this@LoginActivity,"Login Succesful", Toast.LENGTH_LONG).show()
                    goToMainActivity()
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                    finish()
                } else {
                    println("Registration Failed: ${it.exception?.message}")
                    Toast.makeText(this, "Registration Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Geri tuşuna basınca Login ekranına geri dönülmesin
        startActivity(intent)
    }

}