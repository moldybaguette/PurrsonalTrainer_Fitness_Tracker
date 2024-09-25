package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

/**
 * A simple [Fragment] subclass.
 * Use the [CreateCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateCategoryFragment : Fragment() {

    private lateinit var dismissAction: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_category, container, false)

        // Getting the category name from user, made by chat but tested and works
        val categoryNameInput = view.findViewById<EditText>(R.id.customCategoryName)
        categoryNameInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {

                val textFromUser = categoryNameInput.text
                // Do something with the category name here

                // Remove focus from the EditText
                categoryNameInput.clearFocus()

                dismissAction()
                true
            } else {
                false
            }
        }
        return view
    }

    public fun setDismissAction(dismissAction: () -> Unit)
    {
        this.dismissAction = dismissAction
    }
}