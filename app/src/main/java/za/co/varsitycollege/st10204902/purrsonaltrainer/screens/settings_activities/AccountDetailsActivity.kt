package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.MainActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityAccountDetailsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.SettingsActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.DataWipeConfirmationFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.PasswordReentryFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class AccountDetailsActivity : AppCompatActivity(),
    PasswordReentryFragment.OnPasswordReentryListener,
    DataWipeConfirmationFragment.OnDataWipeConfirmationListener {
    private lateinit var binding: ActivityAccountDetailsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        binding.emailInput.setText(currentUser?.email)

        currentUser?.providerData?.forEach { profile ->
            if (profile.providerId == "google.com") {
                binding.emailInput.isEnabled = false
                binding.passwordInput.isEnabled = false
                Toast.makeText(
                    this,
                    "Google users cannot edit email or password",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.doneButtonAccountDetails.setOnClickListener {
            if(binding.passwordInput.text.isNotEmpty()) {
                launchPasswordReentryFragment()
            }
            else{
                navigateTo(this, SettingsActivity::class.java, null)
            }

        }
        binding.resetAppButton.setOnClickListener { launchDataWipeConfirmationFragment() }
        binding.logOutButton.setOnClickListener {
            auth.signOut()
            navigateTo(this, MainActivity::class.java, null)
        }
    }



    override fun onPasswordReentered(password: String) {
        dismissPopup()
        val currentUser = auth.currentUser ?: return showToast("Current password is required")

        val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
        currentUser.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) updateEmailAndPassword()
            else showToast("Re-authentication failed: ${reauthTask.exception?.message}")
        }
    }

    private fun updateEmailAndPassword() {
        val currentUser = auth.currentUser ?: return
        val newPassword = binding.passwordInput.text.toString()

        if (newPassword.isNotEmpty()) {
            currentUser.updatePassword(newPassword).addOnCompleteListener { passwordTask ->
                if (passwordTask.isSuccessful) showToast("Password updated")
                else showToast("Password update failed: ${passwordTask.exception?.message}")
            }
        }

        auth.signOut()
        navigateTo(this, MainActivity::class.java, null)
    }

    private fun launchPasswordReentryFragment() {
        showPopup()
        supportFragmentManager.beginTransaction().apply {
            replace(binding.popupFragmentContainer.id, PasswordReentryFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun launchDataWipeConfirmationFragment() {
        showPopup()
        supportFragmentManager.beginTransaction().apply {
            replace(binding.popupFragmentContainer.id, DataWipeConfirmationFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun showPopup() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.popupFragmentContainer.startAnimation(slideUp)
        binding.popupFragmentContainer.visibility = View.VISIBLE
        binding.dismissArea.visibility = View.VISIBLE
        binding.dismissArea.setOnClickListener { dismissPopup() }
    }

    private fun dismissPopup() {
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.popupFragmentContainer.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.popupFragmentContainer.visibility = View.GONE
                binding.dismissArea.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDataWipeConfirmed() {
        dismissPopup()
        lifecycleScope.launch {
            UserManager.deleteUserDataExceptID()
        }
    }

    override fun onDataWipeCancelled() {
        dismissPopup()
    }
}