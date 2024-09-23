package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.fragment.app.Fragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeLoginRegisterBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.BubbleButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class HomeLoginRegisterActivity : AppCompatActivity() {
    // THIS IS THE FIRST PAGE IN UI FLOW
    private lateinit var binding: ActivityHomeLoginRegisterBinding

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    //TODO Check that the multiple button logic works, need to implement the navigation to check that this logic works.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



        //buttons

        var registerButton = findViewById<AppCompatButton>(R.id.registerButton)
        var SSOButton = findViewById<AppCompatButton>(R.id.ssoButton)

        //onclick listeners
        registerButton.setOnClickListener {
            navigateTo(this,RegisterActivity::class.java, null)
        }
        SSOButton.setOnClickListener {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(getString(R.string.your_web_client_id))
                .setAutoSelectEnabled(true)
                .setNonce("1234567890123456")
                .build()
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