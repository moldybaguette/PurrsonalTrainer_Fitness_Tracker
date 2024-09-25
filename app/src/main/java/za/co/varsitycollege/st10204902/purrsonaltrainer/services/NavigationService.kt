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
 * Navigates to a new activity
 * If you have no data. Simply call this method with two arguments.
 * @param context the current context of the application. Just write 'applicationContext'
 * @param activityToOpen the activity you want to navigate to. Something like
 * 'MainActivity::class.java' yes it should say java
 * @param dataToPass **Set to null if no data to pass** a bundle containing additional data to be
 * passed to the activity being navigated to. You can get this data by calling
 * 'intent.extras?.getBundle("data")' in the onCreate method of the activity being navigated to.
 */
public fun navigateTo(context: Context, activityToOpen: Class<*>, dataToPass: Bundle?)
{
    // declare intent with context and class to pass the value to
    val intent = Intent(context, activityToOpen)

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    // Adding extra data if it exists
    dataToPass?.let { intent.putExtra("data", it) }

    // starting the new activity
    context.startActivity(intent)
}

public class SlideUpPopup(
    private val fragmentManager: FragmentManager,
    private val fragmentContainer: FrameLayout,
    private val dismissArea: View,
    private val fragment: Fragment,
    private val context: Context)
{
    init
    {
        // Binding fragment preemptively
        bindToFragment()
        // Setup dismissArea OnClick
        dismissArea.setOnClickListener { dismissPopup() }
    }

    private fun bindToFragment()
    {
        fragmentManager.beginTransaction().apply {
            replace(fragmentContainer.id, fragment)
            addToBackStack(null)
            commit()
        }
    }

    public fun showPopup()
    {
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        fragmentContainer.startAnimation(slideUp)
        fragmentContainer.visibility = View.VISIBLE
        dismissArea.visibility = View.VISIBLE
    }

    public fun dismissPopup()
    {
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