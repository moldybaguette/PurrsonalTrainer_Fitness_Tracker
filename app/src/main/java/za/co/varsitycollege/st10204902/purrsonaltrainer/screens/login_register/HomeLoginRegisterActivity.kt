package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeLoginRegisterBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.BubbleButton

class HomeLoginRegisterActivity : AppCompatActivity() {
    // THIS IS THE FIRST PAGE IN UI FLOW
    private lateinit var binding: ActivityHomeLoginRegisterBinding

    //TODO Check that the multiple button logic works, need to implement the navigation to check that this logic works.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply login fragment before hand
        this.supportFragmentManager.beginTransaction().apply {
            replace(binding.loginFragmentContainer.id, LoginFragment())
            addToBackStack(null)
            commit()
        }

        // Show popup
        binding.loginButton.setOnClickListener {
            // Show popup
            binding.loginFragmentContainer.visibility = View.VISIBLE
        }
    }
}
