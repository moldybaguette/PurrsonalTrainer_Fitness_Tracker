package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineBuilder


private var exercise: Exercise? = null
private var catagoryID: String? = null

/**
 * A simple [Fragment] subclass.
 * Use the [CreateExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateExerciseFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_create_exercise, container, false)

        var title = view.findViewById<EditText>(R.id.exerciseTitle)
        var workoutType = view.findViewById<EditText>(R.id.workoutTypeSpinner)
        var notes = view.findViewById<EditText>(R.id.notes)
        var doneBTN = view.findViewById<EditText>(R.id.doneButton)

        if (exercise != null) {
            title.setText(exercise!!.exerciseName)
            //TODO: Change to get type of work out
            notes.setText(exercise!!.notes)
        }

        doneBTN.setOnClickListener {
            if(exercise == null){
                val newExercise = Exercise(
                    exerciseName = title.text.toString(),
                    category = catagoryID!!,
                    notes = notes.text.toString(),
                    // set measurement type based on spinner
                    measurementType = workoutType.text.toString(),
                    isCustom = true
                )
                UserManager.addUserExercise(newExercise)
                RoutineBuilder.addExercise(newExercise)
            }else{
                val newExercise = Exercise(
                    exerciseName = title.text.toString(),
                    category = workoutType.text.toString(),
                    notes = notes.text.toString(),
                    // set measurement type based on spinner
                    measurementType = workoutType.text.toString(),
                    isCustom = true
                )
                if(exercise!!.exerciseName != newExercise.exerciseName) {
                    UserManager.updateUserExercise(exercise!!.exerciseID, newExercise)
                }
                if(exercise!!.notes != newExercise.notes) {
                    UserManager.updateUserExercise(exercise!!.exerciseID, newExercise)
                }
                // if they change the measurement type update the exercise
                if(exercise!!.measurementType != newExercise.measurementType) {
                    UserManager.updateUserExercise(exercise!!.exerciseID, newExercise)
                }


                //TODO: Update the exercise in the routine
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(exersise: Exercise?, catagoryID: String) =
            CreateExerciseFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}