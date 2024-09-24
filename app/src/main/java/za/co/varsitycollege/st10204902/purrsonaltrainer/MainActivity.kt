package za.co.varsitycollege.st10204902.purrsonaltrainer

import androidx.biometric.BiometricManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CatFact
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CatImage
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register.HomeLoginRegisterActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.CatFactsApi
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.CatsApiService
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var catFactTextView: TextView
    private lateinit var catImageView: ImageView
    private lateinit var api: CatFactsApi
    private lateinit var catsApiService: CatsApiService
    private lateinit var idToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catFactTextView = findViewById(R.id.catFactTextView)
        catImageView = findViewById(R.id.catImageView)

        val retrofit1 = Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit1.create(CatFactsApi::class.java)
        catsApiService = retrofit2.create(CatsApiService::class.java)


        fetchCatFact()
        loadNewCatImage()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        setupBiometricPrompt()

        // Check if a user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            idToken = currentUser.getIdToken(true).toString()
            checkBiometricSupport()

        } else {
            // No user is logged in, navigate to the login/register screen
            Log.d("MainActivity", "No user logged in")
            navigateTo(this, HomeLoginRegisterActivity::class.java, null)
        }
    }

    private fun loadNewCatImage() {
        catsApiService.getRandomCatImage().enqueue(object : Callback<List<CatImage>> {
            override fun onResponse(call: Call<List<CatImage>>, response: Response<List<CatImage>>) {
                if (response.isSuccessful && response.body() != null) {
                    val catImageUrl = response.body()!![0].url
                    Picasso.get().load(catImageUrl).into(catImageView)
                }
            }

            override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun fetchCatFact() {
        api.getCatFact().enqueue(object : Callback<CatFact> {
            override fun onResponse(call: Call<CatFact>, response: Response<CatFact>) {
                if (response.isSuccessful) {
                    catFactTextView.text = response.body()?.fact
                } else {
                    catFactTextView.text = "Error fetching cat fact"
                }
            }

            override fun onFailure(call: Call<CatFact>, t: Throwable) {
                catFactTextView.text = "Failed to load cat fact"
            }
        })
    }


    private fun checkBiometricSupport() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> authenticate()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(
                this,
                "No biometric features available on this device.",
                Toast.LENGTH_SHORT
            ).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(
                this,
                "Biometric features are currently unavailable.",
                Toast.LENGTH_SHORT
            ).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> navigateTo(
                this,
                HomeLoginRegisterActivity::class.java,
                null
            )
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateTo(this@MainActivity, HomeLoginRegisterActivity::class.java, null)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()

                    UserManager.userManagerScope.launch {
                        async {
                            UserManager.setUpSingleton(idToken)
                        }.await()
                        navigateTo(this@MainActivity, HomeActivity::class.java, null)
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateTo(this@MainActivity, HomeLoginRegisterActivity::class.java, null)
                }
            })


        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }
}