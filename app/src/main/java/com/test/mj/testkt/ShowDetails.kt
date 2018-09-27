package com.test.mj.testkt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_show_details.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import java.net.URL
import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import org.json.JSONException
import java.net.MalformedURLException


class ShowDetails : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn){
            Toast.makeText(this, "User FB Signed In", Toast.LENGTH_LONG).show()
            val fbProfile= Profile.getCurrentProfile()
            val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
                Log.i("LoginActivity", response.toString())
                // Get facebook data from login
                val bFacebookData = getFacebookData(`object`)

                if (bFacebookData != null) {
                    tvEmail.setText(bFacebookData.get("email").toString())
                }
            }
            val parameters = Bundle()
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location") // Parámetros que pedimos a facebook
            request.parameters = parameters
            request.executeAsync()
            tvName.setText(fbProfile.name.toString())

            Glide.with(this).load(fbProfile.getProfilePictureUri(180,180)).into(ivPhoto);

        }
        else{
            Toast.makeText(this, "NO FB user Signed In",Toast.LENGTH_LONG).show()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        var account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            Log.d(TAG, "one Google user is signed in")
            tvName.setText(account.displayName.toString())
            tvEmail.setText(account.email.toString())
            Glide.with(this).load(account.photoUrl).into(ivPhoto);
        }else{
            Log.d(TAG, "NOOO Google user is signed in")
        }
        btnDetailSignOut.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener{
                startActivity(Intent(this, MainActivity::class.java))
            }
            if(isLoggedIn){
                LoginManager.getInstance().logOut()
                startActivity(Intent(this, MainActivity::class.java))
            }

        }
    }

    private fun getFacebookData(`object`: JSONObject): Bundle? {

        try {
            val bundle = Bundle()
            val id = `object`.getString("id")

            try {
                val profile_pic = URL("https://graph.facebook.com/$id/picture?width=200&height=150")
                Log.i("profile_pic", profile_pic.toString() + "")
                bundle.putString("profile_pic", profile_pic.toString())

            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return null
            }

            bundle.putString("idFacebook", id)
            if (`object`.has("first_name"))
                bundle.putString("first_name", `object`.getString("first_name"))
            if (`object`.has("last_name"))
                bundle.putString("last_name", `object`.getString("last_name"))
            if (`object`.has("email"))
                bundle.putString("email", `object`.getString("email"))
            if (`object`.has("gender"))
                bundle.putString("gender", `object`.getString("gender"))
            if (`object`.has("birthday"))
                bundle.putString("birthday", `object`.getString("birthday"))
            if (`object`.has("location"))
                bundle.putString("location", `object`.getJSONObject("location").getString("name"))

            return bundle
        } catch (e: JSONException) {
            Log.d(TAG, "Error parsing JSON")
        }

        return null
    }

}
