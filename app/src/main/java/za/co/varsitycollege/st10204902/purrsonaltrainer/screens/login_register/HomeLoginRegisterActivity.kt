package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.BubbleButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class HomeLoginRegisterActivity : AppCompatActivity() {
    // THIS IS THE FIRST PAGE IN UI FLOW
    private lateinit var binding: ActivityHomeBinding

    //TODO Check that the multiple button logic works, need to implement the navigation to check that this logic works
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_login_register)



        //buttons
        var loginButton = findViewById<AppCompatButton>(R.id.loginButton)
        var registerButton = findViewById<AppCompatButton>(R.id.registerButton)
        var SSOButton = findViewById<AppCompatButton>(R.id.ssoButton)

        //onclick listeners
        loginButton.setOnClickListener {
            //navigateTo(this,LoginFragment::class.java, null)
            Toast.makeText(this@HomeLoginRegisterActivity, "feature not ready", Toast.LENGTH_SHORT).show()
        }
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
}
