package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityBreakdownBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.AnalysisFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.RecordsFragment

/**
 * Activity for displaying breakdown information using a ViewPager2.
 */
class BreakdownActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreakdownBinding

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = ScreenSlidePagerAdapter(this)
    }

    /**
     * Adapter for managing the fragments in the ViewPager2.
     * @param activity The activity where the ViewPager2 is used.
     */
    private inner class ScreenSlidePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        /**
         * Returns the number of fragments.
         * @return The number of fragments.
         */
        override fun getItemCount(): Int = 2

        /**
         * Creates a new fragment for the given position.
         * @param position The position of the fragment within the adapter.
         * @return The fragment for the given position.
         */
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AnalysisFragment()
                else -> RecordsFragment()
            }
        }
    }
}