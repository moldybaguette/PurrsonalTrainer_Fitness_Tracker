package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeLoginRegisterBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class HomeLoginRegisterActivity : AppCompatActivity() {


    //TODO
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    // THIS IS THE FIRST PAGE IN UI FLOW
    private lateinit var binding: ActivityHomeLoginRegisterBinding

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    //TODO Check that the multiple button logic works, need to implement the navigation to check that this logic works.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setContentView(R.layout.activity_home_login_register)
        binding = ActivityHomeLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply login fragment before hand
        populateLoginFragment()


        val registerButton: ImageView = findViewById(R.id.registerButton)
        val originalBackgroundRegister = registerButton.background //getting the original background of the button
        registerButton.setOnClickListener {
            navigateTo(this, RegisterActivity::class.java, null)
            registerButton.setBackgroundResource(R.drawable.svg_purple_bblbtn_clicked) // Set the background to the clicked background
            Handler(Looper.getMainLooper()).postDelayed({
                registerButton.background = originalBackgroundRegister
            }, 400)
        }

        val loginButton: ImageView = findViewById(R.id.loginButton)
        val originalBackgroundLogin = loginButton.background
        // Binding show and dismiss popup for login
        binding.loginButton.setOnClickListener {
            showLoginPopup()
            loginButton.setBackgroundResource(R.drawable.svg_orange_bblbtn_clicked) // Set the background to the clicked background
            Handler(Looper.getMainLooper()).postDelayed({
                loginButton.background = originalBackgroundLogin
            }, 400)}
        binding.loginDismissArea.setOnClickListener { dismissLoginPopup() }
    }

    // Login Popup Methods
    //-----------------------------------------------------------//

    private fun showLoginPopup()
    {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.loginFragmentContainer.startAnimation(slideUp)
        binding.loginFragmentContainer.visibility = View.VISIBLE
        binding.loginDismissArea.visibility = View.VISIBLE
    }

    private fun dismissLoginPopup()
    {
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

        //TODO
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //buttons
        var loginButton = findViewById<ImageView>(R.id.loginButton)
        var registerButton = findViewById<AppCompatButton>(R.id.registerButton)
        val googleSignInButton = findViewById<SignInButton>(R.id.googleSignInButton)

        //onclick listeners
        loginButton.setOnClickListener {
            //navigateTo(this,LoginFragment::class.java, null)
            Toast.makeText(this@HomeLoginRegisterActivity, "feature not ready", Toast.LENGTH_SHORT).show()
        }
        registerButton.setOnClickListener {
            navigateTo(this,RegisterActivity::class.java, null)
        }
        findViewById<SignInButton>(R.id.googleSignInButton).setOnClickListener {
            //TODO
            signIn()
        }
    }

    //TODO
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    //TODO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    //TODO
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Google Sign In was successful, authenticate with your backend
            account?.idToken?.let { token ->
                CoroutineScope(Dispatchers.Main).launch {
                    var authManager = AuthManager()
                    val result = authManager.signInWithSSO(token)
                    if (result.isSuccess) {
                        // Handle successful sign-in
                        Toast.makeText(this@HomeLoginRegisterActivity, "Sign-in successful", Toast.LENGTH_SHORT).show()
                        // Navigate to the next screen or update UI
                        navigateTo(this@HomeLoginRegisterActivity, HomeActivity::class.java, null)
                    } else {
                        // Handle sign-in failure
                        Toast.makeText(this@HomeLoginRegisterActivity, "Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: ApiException) {
            // Google Sign In failed, handle the error
            Log.e(TAG, "Google sign in failed", e)
            val errorMessage = when (e.statusCode) {
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Google Sign In was cancelled"
                GoogleSignInStatusCodes.NETWORK_ERROR -> "Network error occurred"
                GoogleSignInStatusCodes.INVALID_ACCOUNT -> "Invalid account"
                GoogleSignInStatusCodes.SIGN_IN_REQUIRED -> "Sign In required"
                else -> "Google Sign In failed: ${e.statusCode}"
            }
            // Google Sign In failed, update UI appropriately
            Toast.makeText(this@HomeLoginRegisterActivity, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }
    }



    private fun populateLoginFragment() {
        this.supportFragmentManager.beginTransaction().apply {
            replace(binding.loginFragmentContainer.id, LoginFragment())
            addToBackStack(null)
            commit()
        }
    }
}
//------------------------***EOF***-----------------------------//