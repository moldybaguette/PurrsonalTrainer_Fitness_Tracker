package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
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
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class HomeLoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    //TODO
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_home_login_register)

        //TODO
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //buttons
        var loginButton = findViewById<AppCompatButton>(R.id.loginButton)
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
}
