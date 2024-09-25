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

class BreakdownActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreakdownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter=ScreenSlidePagerAdapter(this)
    }
    private inner class ScreenSlidePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AnalysisFragment()
                else -> RecordsFragment()
            }
        }
    }
}