package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: ImageView = view.findViewById(R.id.loginButton)
        val originalBackgroundLogin = loginButton.background

        loginButton.setOnClickListener {
            loginButton.setBackgroundResource(R.drawable.svg_orange_bblbtn_clicked)

            // Revert the background after 2 seconds (2000 milliseconds)
            Handler(Looper.getMainLooper()).postDelayed({
                loginButton.background = originalBackgroundLogin
            }, 400)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
            }
    }
}