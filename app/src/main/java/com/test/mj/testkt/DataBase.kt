package com.test.mj.testkt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_data_base.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class DataBase : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    // Write a message to the database
    var mRootDatabase = FirebaseDatabase.getInstance().getReference("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_base)
        mAuth = FirebaseAuth.getInstance()
        btnInsert.setOnClickListener {
            verify()

        }
        btnSignOut.setOnClickListener {
            signOut()
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this@DataBase, MainActivity::class.java))
        }
    }

    private fun signOut() {
        if(mAuth !=null){
            mAuth.signOut()
        }
    }

    private fun verify() {
        var mDatabase= mAuth.uid?.let { it1 -> mRootDatabase.child(it1) }
        if (mDatabase != null) {
            Log.d("here", mAuth.uid)
            if (!etFName.text.isEmpty()) {
                val fName = etFName.text.toString()
                if (!etLName.text.isEmpty()) {
                    val lName = etLName.text.toString()
                    if (!etPassword.text.isEmpty()) {
                        val pass = etPassword.text.toString()
                        if (!etPhoneNo.text.isEmpty()) {
                            val phoneNo = etPhoneNo.text.toString()
                            mDatabase.child("phoneNo").setValue(phoneNo)
                            mDatabase.child("fName").setValue(fName)
                            mDatabase.child("lName").setValue(lName)
                            mDatabase.child("email").setValue(etEmail.text.toString())
                            mDatabase.child("password").setValue(pass)
                        } else {
                            mDatabase.child("fName").setValue(fName)
                            mDatabase.child("lName").setValue(lName)
                            mDatabase.child("email").setValue(etEmail.text.toString())
                            mDatabase.child("password").setValue(pass)

                        }

                    }
                } else {
                }
            } else {
            }
        } else {
            Log.d("here", "not available")

            if (!etFName.text.isEmpty()) {
                val fName = etFName.text.toString()
                if (!etLName.text.isEmpty()) {
                    val lName = etLName.text.toString()
                    if(!etEmail.text.isEmpty()){
                        val email = etEmail.text.toString()
                        if (!etPassword.text.isEmpty()) {
                            val pass = etPassword.text.toString()
                            mAuth.createUserWithEmailAndPassword(email,pass)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success")
                                            //startActivity(Intent(applicationContext, DataBase::class.java))
                                            Toast.makeText(this, "createUserWithEmail:success, LogIn Now}", Toast.LENGTH_SHORT).show()
                                            verify()

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                            Toast.makeText(this, "Authentication failed.${task.exception.toString()}",
                                                    Toast.LENGTH_SHORT).show()
                                        }

                                        // ...
                                    }

                        }
                    }else{

                    }

                } else {
                }
            } else {
            }


        }    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        autoFil(currentUser)
    }

    private fun autoFil(user: FirebaseUser?) {
        if(user !=null){
            etEmail.setText(user.email)

        }

    }
}
