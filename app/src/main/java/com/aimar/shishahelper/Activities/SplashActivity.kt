package com.aimar.shishahelper.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.aimar.shishahelper.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity: AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shishasplash)

        //Thread.sleep(3000)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            mAuth = FirebaseAuth.getInstance()

            if (mAuth.currentUser != null) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.

    }

}