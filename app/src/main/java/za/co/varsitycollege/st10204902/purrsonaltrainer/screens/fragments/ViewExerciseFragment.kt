package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.GradientEditText
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
        val view = inflater.inflate(R.layout.fragment_view_exercise, container, false)

        var exerciseTitle = view.findViewById<GradientEditText>(R.id.exerciseTitle)
        var categoryLabel = view.findViewById<TextView>(R.id.categoryLabel)
        var exerciseDescription = view.findViewById<ScrollView>(R.id.exerciseDescription)

        if (exercise != null) {
            exerciseTitle.setText(exercise!!.exerciseName)
            categoryLabel.text = exercise!!.category
            exerciseDescription.findViewById<TextView>(R.id.exerciseDescriptionText).text = exercise!!.notes
        }
        else{
            Log.d("ViewExerciseFragment", "Exercise is null")
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(exersise: Exercise) =
            ViewExerciseFragment().apply {
                arguments = Bundle().apply {
                    exercise = exersise
                }
            }
    }
}