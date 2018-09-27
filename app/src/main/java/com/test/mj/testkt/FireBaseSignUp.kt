package com.test.mj.testkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_fire_base_sign_up.*
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class FireBaseSignUp : AppCompatActivity() {
lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_sign_up)
        mAuth = FirebaseAuth.getInstance();
        val btnBackMain=findViewById<Button>(R.id.btnBackMain)
        btnBackMain.setOnClickListener{
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }
        val btnFBSIGNUP= findViewById<Button>(R.id.btnFBSIGNUP)
        btnFBSIGNUP.setOnClickListener {
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        if (!etFN.text.isEmpty()){
            val FName=etFN.text.toString()
            if (!etLN.text.isEmpty()){
                val LName=etLN.text.toString()
                if (!etEmail.text.isEmpty()){
                    val email=etEmail.text.toString()
                    if(!etPassword.text.isEmpty()){
                        val password=etPassword.text.toString()
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success")
                                        val user = mAuth.getCurrentUser()
                                        startActivity(Intent(applicationContext, FireBaseLogIn::class.java))
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                        Toast.makeText(this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show()
                                    }

                                    // ...
                                })
                    }else {tvError.setText("Enter Your Password")}
                }else {tvError.setText("Enter Your Email")}

            }else {tvError.setText("Enter Your Last Name")}

        } else {tvError.setText("Enter Your First Name")}
    }
}
