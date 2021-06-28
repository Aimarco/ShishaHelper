package com.aimar.shishahelper.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aimar.shishahelper.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var signUpButton:Button
    lateinit var signInButton:Button
    lateinit var editTextUsername : EditText
    lateinit var editTextpassword:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.shishasplash)
        //SPLASH
        Thread.sleep(3000)

        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        setContentView(R.layout.layout_auth)


        // inicializar campos
        signInButton = findViewById(R.id.SignInButton)
        signUpButton = findViewById(R.id.signUpButton)
        editTextUsername=findViewById(R.id.editTextUsername)
        editTextpassword = findViewById(R.id.passwordEditText)

        //Analytics event
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion anaytics")
        analytics.logEvent("InitScreeen", bundle)

        setup()

    }

        private fun setup() {
            title = "Inicio sesión"
            signUpButton.setOnClickListener {
                if (editTextUsername.text.isNotEmpty() && editTextpassword.text.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        editTextUsername.text.toString(),
                        editTextpassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "")
                        } else {
                            showAlert()
                        }

                    }

                }

            }

            signInButton.setOnClickListener {
                if (editTextUsername.text.isNotEmpty() && editTextpassword.text.isNotEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        editTextUsername.text.toString(),
                        editTextpassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {

                            showHome(it.result?.user?.email ?: "")
                        } else {
                            showAlert()
                        }

                    }

                }

            }
        }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }

    private fun showHome(email: String){


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val homeIntent = Intent(this, HomeActivity::class.java).apply {
                putExtra("email", email)
            }
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(homeIntent)
        } else {
            // User is signed out
            Log.d("LOGOT", "onAuthStateChanged:signed_out")
        }

    }



}