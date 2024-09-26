package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise


private var exercise: Exercise? = null

/**
 * A simple [Fragment] subclass.
 * Use the [ViewExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewExerciseFragment : Fragment() {
    private var exercise: Exercise? = null

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
        return inflater.inflate(R.layout.fragment_view_exercise, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(exersise: Exercise) =
            ViewExerciseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}