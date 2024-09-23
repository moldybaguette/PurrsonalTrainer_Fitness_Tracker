package za.co.varsitycollege.st10204902.purrsonaltrainer.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.CatFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.HomeFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.RoutinesFragment

/**
 * Object holding useful fragment manipulation functions
 */
object FragmentUtils
{
    //-----------------------------------------------------------//
    //                          PROPERTIES                       //
    //-----------------------------------------------------------//

    public lateinit var supportFragmentManager: FragmentManager

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    /**
     * Changes the fragment shown to the one given.
     * How to use: 'FragmentUtils.navigateToFragment(DashboardFragment())' where 'DashboardFragment()' is the method
     * used to create the fragment you want to navigate to.
     * NOTE THIS WILL NOT WORK IF THE LANDING ACTIVITY HAS NEVER BEEN NAVIGATED TO
     * @param fragment: the fragment you want to navigate to. E.g. DashboardFragment()
     */
    fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }
}

class HomeActivity : AppCompatActivity() {
    //THIS IS THE FRAGMENT MANAGER PAGE
    //TABS: HOME, ROUTINES, CAT
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation Code:
        FragmentUtils.supportFragmentManager = this.supportFragmentManager

        // Setting initial fragment shown
        FragmentUtils.navigateToFragment(HomeFragment())

        // Assigning navigation paths to bottom navigation menu items
        binding.bottomNavigationView.setOnItemSelectedListener()
        {
            when(it.itemId)
            {
                R.id.homeMenuItem -> FragmentUtils.navigateToFragment(HomeFragment())
                R.id.routinesMenuItem -> FragmentUtils.navigateToFragment(RoutinesFragment())
                R.id.catMenuItem -> FragmentUtils.navigateToFragment(CatFragment())
            }
            true
        }
    }
}

//------------------------***EOF***-----------------------------//