package com.test.mj.testkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.password
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


val TAG="MainActivity"
val RC_SIGN_IN=1
var FB_SIGN_IN=1
class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)

        val EMAIL = "email"
        val loginButton= findViewById<LoginButton>(R.id.login_button_Facebook)
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"))

    callbackManager = CallbackManager.Factory.create();
        FB_SIGN_IN=2

    LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {
                    tvShow.setText("successssssss")
                    tvShow.visibility=View.VISIBLE

                    startActivity(Intent(applicationContext, ShowDetails::class.java))
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    tvShow.setText("errrrror${error.toString()}")
                }

            });
/*        try {
            val info = packageManager.getPackageInfo(
                    "com.test.mj.testkt",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }*/
        mAuth = FirebaseAuth.getInstance()
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            Log.d(TAG, "one Google user is signed in")
        }
        sign_in_button_google.setOnClickListener {
            signInGoogle();
        }
        val btnSignUp=findViewById<Button>(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            signUpNewUser()
        }
        btnMainSignOut.setOnClickListener {
            signOut()
        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, DataBase::class.java))
        }
        val btnFirebaseSignIn=findViewById<Button>(R.id.btnFirebaseSignIn)
        btnFirebaseSignIn.setOnClickListener({
            val email= teEmail.text.toString()
            val password= tePassword.text.toString()
            if (!email.isEmpty() && !password.isEmpty() ){
                Toast.makeText(this, "Logging in",Toast.LENGTH_SHORT).show()
                authUsers(email, password)
            }else{
                Toast.makeText(this, "Please Fill Your Cridentials",Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun signUpNewUser() {
        if (!teEmail.text.isEmpty() && !tePassword.text.isEmpty()) {
            val email = teEmail.text.toString()
            val password = tePassword.text.toString()
            email?.let {
                password?.let { it1 ->
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success")
                                    //startActivity(Intent(applicationContext, DataBase::class.java))
                                    Toast.makeText(this, "createUserWithEmail:success, LogIn Now}",Toast.LENGTH_SHORT).show()
                                    authUsers(email, password)

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(this, "Authentication failed.${task.exception.toString()}",
                                            Toast.LENGTH_SHORT).show()
                                }

                                // ...
                            }
                }
            }
        } else {
            Toast.makeText(this, "Please Fill Your Cridentials", Toast.LENGTH_LONG).show()
        }
    }

    private fun authUsers(email:String , password: String) {
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

    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            tvShow.setText(user.email.toString())
            tvShow.visibility=View.VISIBLE
        }

    }


    override fun onStart() {
        super.onStart()
           // signOut()
    }

    private fun signOut() {
        if(mAuth !=null){
            tvShow.visibility=View.INVISIBLE
            mAuth.signOut()
        }
        if(mGoogleSignInClient!=null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, "GoogleSignedOut Successfully",
                    Toast.LENGTH_SHORT).show()
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                AccessToken.setCurrentAccessToken(null)
                LoginManager.getInstance().logOut()

                finish()
            }).executeAsync()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
           // updateUI(account)
            Toast.makeText(this, "GoogleSignedIn Successfully",
                    Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ShowDetails::class.java))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }

    }



}
