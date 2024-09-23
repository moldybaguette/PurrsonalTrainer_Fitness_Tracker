package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

//------------------------------------------navigateTo()------------------------------------------//
/**
 * Navigates to a new activity
 * If you have no data. Simply call this method with two arguments.
 * @param context the current context of the application. Just write 'applicationContext'
 * @param activityToOpen the activity you want to navigate to. Something like
 * 'MainActivity::class.java' yes it should say java
 * @param dataToPass a bundle containing additional data to be passed to the activity being
 * navigated to. You can get this data by calling 'intent.extras?.getBundle("data")' in
 * the onCreate method of the activity being navigated to.
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
//....................................._____End of File_____......................................//