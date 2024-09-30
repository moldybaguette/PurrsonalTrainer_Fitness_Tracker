package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.SoundManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

/**
 * A fragment that handles user login.
 */
class LoginFragment : Fragment() {

    private lateinit var soundManager: SoundManager

    /**
     * Called to do initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        soundManager = SoundManager(requireContext(), R.raw.custom_tap_sound)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: ImageView? = view.findViewById(R.id.loginButton)
        val email: EditText? = view.findViewById(R.id.emailInput)
        val password: EditText? = view.findViewById(R.id.passwordInput)
        val originalBackgroundLogin = loginButton?.background
        val emailLabel: View? = view.findViewById(R.id.emailLabel)
        val passwordLabel: View? = view.findViewById(R.id.passwordLabel)

        loginButton?.setOnClickListener {
            soundManager.playSound()

            loginButton.setBackgroundResource(R.drawable.svg_orange_bblbtn_clicked)

            // Revert the background after 2 seconds (2000 milliseconds)
            Handler(Looper.getMainLooper()).postDelayed({
                loginButton.background = originalBackgroundLogin
            }, 400)

            LoginUser(email?.text.toString(), password?.text.toString())
        }

        applyFloatUpAnimation(loginButton)
        applyFloatUpAnimation(email)
        applyFloatUpAnimation(password)
        applyFloatUpAnimation(emailLabel)
        applyFloatUpAnimation(passwordLabel)
    }

    /**
     * Logs in the user with the provided email and password.
     * @param email The user's email.
     * @param password The user's password.
     */
    private fun LoginUser(email: String, password: String) {
        var authManager = AuthManager()
        CoroutineScope(Dispatchers.IO).launch {
            val result = authManager.loginUser(email, password)
            if (result.isSuccess) {
                val data = result.getOrNull()
                if (data != null) {
                    try {
                        UserManager.userManagerScope.launch {
                            async {
                                UserManager.setUpSingleton(data.toString())
                            }.await()
                            navigateTo(requireContext(), HomeActivity::class.java, null)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error setting up user", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Unable to login with supplied details. Please try again.", Toast.LENGTH_SHORT).show()
                }
                Log.e("LoginFragment", "error: ${result.exceptionOrNull()}")
            }
        }
    }

    private fun applyFloatUpAnimation(view: View?) {
        view?.let {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.float_up)
            it.startAnimation(animation)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
            }
    }
}