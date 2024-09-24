package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.util.UUID
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeLoginRegisterBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class HomeLoginRegisterActivity : AppCompatActivity() {
    //-----------------------------------------------------------//
    //                          VARIABLES                        //
    //-----------------------------------------------------------//
    private lateinit var binding: ActivityHomeLoginRegisterBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val TAG = "GoogleSignIn"
    private val REQ_ONE_TAP = 2
    private lateinit var auth: FirebaseAuth

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    //TODO Check that the multiple button logic works, need to implement the navigation to check that this logic works.
    /**
     * Called when the activity is starting.
     * This method will initialize the activity and set up the One Tap API.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     * Otherwise, it is null.
     * @see onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize One Tap API
        oneTapClient = Identity.getSignInClient(this)

        // Initialize One Tap sign in request
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.WEB_CLIENT_ID_FIREBASE))
                    .setFilterByAuthorizedAccounts(false) // Ensure this is set to false
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()

        // Apply login fragment before hand
        populateLoginFragment()


        // Binding the register button
        val originalBackgroundRegister =
            binding.registerButton.background //getting the original background of the button
        // Setting the click listener for the register button
        binding.registerButton.setOnClickListener {
            navigateTo(this, RegisterActivity::class.java, null)
            binding.registerButton.setBackgroundResource(R.drawable.svg_purple_bblbtn_clicked) // Set the background to the clicked background
            Handler(Looper.getMainLooper()).postDelayed({
                binding.registerButton.background = originalBackgroundRegister
            }, 400)
        }

        // Binding the login button

        val originalBackgroundLogin = binding.loginButton.background
        // Binding show and dismiss popup for login
        binding.loginButton.setOnClickListener {
            showLoginPopup()
            binding.loginButton.setBackgroundResource(R.drawable.svg_orange_bblbtn_clicked) // Set the background to the clicked background
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loginButton.background = originalBackgroundLogin
            }, 400)
        }
        binding.loginDismissArea.setOnClickListener { dismissLoginPopup() }

        binding.ssoButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Login Popup Methods
    //-----------------------------------------------------------//

    /**
     * Shows the login popup.
     * This method will animate the login fragment container to slide up and then show the container.
     */
    private fun showLoginPopup() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.loginFragmentContainer.startAnimation(slideUp)
        binding.loginFragmentContainer.visibility = View.VISIBLE
        binding.loginDismissArea.visibility = View.VISIBLE
    }


    /**
     * Dismisses the login popup.
     * This method will animate the login fragment container to slide down and then hide the container.
     */
    private fun dismissLoginPopup() {
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.loginFragmentContainer.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.loginFragmentContainer.visibility = View.GONE
                binding.loginDismissArea.visibility = View.GONE
                // Reset the login fragment
                populateLoginFragment()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    /**
     * Populates the login fragment in the login fragment container.
     * This method replaces the current fragment in the container with a new instance of `LoginFragment`.
     */
    private fun populateLoginFragment() {
        this.supportFragmentManager.beginTransaction().apply {
            replace(binding.loginFragmentContainer.id, LoginFragment())
            addToBackStack(null)
            commit()
        }
    }

    /**
     * Signs in with Google using the One Tap API.
     * This method will start the One Tap UI to sign in with Google.
     */
    private fun signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            try {
                startIntentSenderForResult(
                    result.pendingIntent.intentSender, REQ_ONE_TAP,
                    null, 0, 0, 0, null
                )
            } catch (e: Exception) {
                Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }.addOnFailureListener(this) { e ->
            Log.e(TAG, "Google sign in failed: ${e.localizedMessage}")
            e.localizedMessage?.let { Log.d(TAG, it) }
        }
    }


    /**
     * Handles the result of the One Tap sign in.
     * This method will be called when the One Tap sign in activity completes.
     * @param requestCode The request code of the activity
     * @param resultCode The result code of the activity
     * @param data The intent data from the activity
     * @see onActivityResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ONE_TAP) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                val displayName = credential.displayName
                val email = credential.id
                println("$idToken We got the token")
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success")
                                    val user = auth.currentUser
                                    val authManager = AuthManager(auth)
                                    authManager.createUserInRealtimeDatabase(auth.currentUser!!.uid)
                                    navigateTo(this, HomeActivity::class.java, null)

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }

                    else -> {
                        // Shouldn't happen.
                        Log.d(TAG, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                Log.e(TAG, "One Tap failed: ${e.localizedMessage}")
            }
        }
    }
}
//------------------------***EOF***-----------------------------//