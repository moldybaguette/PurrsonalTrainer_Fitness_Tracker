package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import za.co.varsitycollege.st10204902.purrsonaltrainer.MainActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityAccountDetailsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.PasswordReentryFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo
import java.util.concurrent.CountDownLatch

class AccountDetailsActivity : AppCompatActivity(), PasswordReentryFragment.OnPasswordReentryListener {
    private lateinit var binding: ActivityAccountDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var password: String
    private val latch = CountDownLatch(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        binding.emailInput.setText(auth.currentUser?.email)

        // Get provider type from firebase provider
        currentUser?.let { user ->
            val providerData = user.providerData
            for (profile in providerData) {
                when (profile.providerId) {
                    "google.com" -> {
                        binding.emailInput.isEnabled = false
                        binding.passwordInput.isEnabled = false
                        Toast.makeText(this, "Google users cannot edit email or password", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.doneButtonAccountDetails.setOnClickListener {
            launchPasswordReentryFragment()
        }
    }

    override fun onPasswordReentered(password: String) {
        dismissPasswordPopup()

        val currentUser = auth.currentUser

        if (currentUser == null || password.isEmpty()) {
            Toast.makeText(this, "Current password is required", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
        currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                // Re-authentication succeeded
                updateEmailAndPassword()
            } else {
                // Re-authentication failed
                Toast.makeText(
                    this,
                    "Re-authentication failed: ${reauthTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateEmailAndPassword() {
        val currentUser = auth.currentUser ?: return
        val newEmail = binding.emailInput.text.toString()
        val newPassword = binding.passwordInput.text.toString()
        var updateSuccess = true

        // Update Email
        if (newEmail.isNotEmpty() && newEmail != currentUser.email) {
            currentUser.updateEmail(newEmail).addOnCompleteListener { emailTask ->
                if (emailTask.isSuccessful) {
                    Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                } else {
                    updateSuccess = false
                    Toast.makeText(
                        this,
                        "Email update failed: ${emailTask.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Update Password
        if (newPassword.isNotEmpty()) {
            currentUser.updatePassword(newPassword).addOnCompleteListener { passwordTask ->
                if (passwordTask.isSuccessful) {
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                } else {
                    updateSuccess = false
                    Toast.makeText(
                        this,
                        "Password update failed: ${passwordTask.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Optional: Navigate away or refresh UI if updates were successful
        if (updateSuccess) {
            // Reset the app to MainActivity
            auth.signOut()
            navigateTo(this, MainActivity::class.java, null)
        }
    }

    private fun launchPasswordReentryFragment() {
        showPasswordPopup()
        populatePasswordFragment()
    }

    private fun showPasswordPopup() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.PasswordReentryFragmentContainer.startAnimation(slideUp)
        binding.PasswordReentryFragmentContainer.visibility = View.VISIBLE
        binding.passwordDismissArea.visibility = View.VISIBLE
    }

    private fun dismissPasswordPopup() {
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.PasswordReentryFragmentContainer.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.PasswordReentryFragmentContainer.visibility = View.GONE
                binding.passwordDismissArea.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun populatePasswordFragment() {
        this.supportFragmentManager.beginTransaction().apply {
            replace(binding.PasswordReentryFragmentContainer.id, PasswordReentryFragment())
            addToBackStack(null)
            commit()
        }
    }
}