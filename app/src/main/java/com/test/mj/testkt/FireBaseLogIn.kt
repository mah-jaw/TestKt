package com.test.mj.testkt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_fire_base_log_in.*
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


val TAG1: String="FireBaseLogIN"
class FireBaseLogIn : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_log_in)
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance()
        val btnFireBaseSignOut= findViewById<Button>(R.id.btnFireBaseSignOut)
        btnFireBaseSignOut.setOnClickListener {
            Log.d(TAG, "Signning out....")
            mAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

        val email:String?=intent.getStringExtra("email")
        val password:String?=intent.getStringExtra("password")
        email?.let {
            password?.let { it1 ->
                mAuth.signInWithEmailAndPassword(it, it1)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = mAuth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                updateUI(null)
                            }

                            // ...
                        }
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.getCurrentUser()
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            tvName.setText(user.displayName)
            tvEmail.setText(user.email)

        }

    }
}
