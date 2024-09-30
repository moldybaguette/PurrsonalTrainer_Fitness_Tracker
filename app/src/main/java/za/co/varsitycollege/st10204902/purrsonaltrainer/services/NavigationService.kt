package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.CreateCategoryFragment

//-----------------------------------------------------------//
//                          METHODS                          //
//-----------------------------------------------------------//

/**
 * Navigates to a new activity.
 * If you have no data, simply call this method with two arguments.
 *
 * @param context The current context of the application. Just write 'applicationContext'.
 * @param activityToOpen The activity you want to navigate to. Something like 'MainActivity::class.java'.
 * @param dataToPass **Set to null if no data to pass** A bundle containing additional data to be
 * passed to the activity being navigated to. You can get this data by calling
 * 'intent.extras?.getBundle("data")' in the onCreate method of the activity being navigated to.
 */
fun navigateTo(context: Context, activityToOpen: Class<*>, dataToPass: Bundle?) {
    // Declare intent with context and class to pass the value to
    val intent = Intent(context, activityToOpen)

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    // Adding extra data if it exists
    dataToPass?.let { intent.putExtra("data", it) }

    // Starting the new activity
    context.startActivity(intent)
}

/**
 * A class representing a slide-up popup.
 *
 * @param fragmentManager The FragmentManager to handle fragment transactions.
 * @param fragmentContainer The container where the fragment will be placed.
 * @param dismissArea The view area that, when clicked, will dismiss the popup.
 * @param fragment The fragment to be displayed in the popup.
 * @param context The context in which the popup is displayed.
 */
class SlideUpPopup(
    private val fragmentManager: FragmentManager,
    private val fragmentContainer: FrameLayout,
    private val dismissArea: View,
    private val fragment: Fragment,
    private val context: Context
) {
    init {
        // Binding fragment preemptively
        bindToFragment()
        // Setup dismissArea OnClick
        dismissArea.setOnClickListener { dismissPopup() }
    }

    /**
     * Binds the fragment to the fragment container.
     */
    private fun bindToFragment() {
        fragmentManager.beginTransaction().apply {
            replace(fragmentContainer.id, fragment)
            addToBackStack(null)
            commit()
        }
    }

    /**
     * Shows the popup with a slide-up animation.
     */
    fun showPopup() {
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        fragmentContainer.startAnimation(slideUp)
        fragmentContainer.visibility = View.VISIBLE
        dismissArea.visibility = View.VISIBLE
    }

    /**
     * Dismisses the popup with a slide-down animation.
     */
    fun dismissPopup() {
        val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        fragmentContainer.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                dismissArea.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animation?) {
                fragmentContainer.visibility = View.GONE
                // Reset the login fragment
                bindToFragment()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}
//------------------------***EOF***-----------------------------//