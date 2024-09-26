package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CategoryAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.ExerciseAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseService
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineBuilder

class AddExerciseListFragment : Fragment() {
    private var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_exercise_list, container, false)

        var exerciseService = ExerciseService(requireContext())
        //maintain a list of ALL exercises in the category
        val fullListOfCategoryExercises = exerciseService.getExerciseInCategory(categoryId!!)
        var displayedExerciseList = fullListOfCategoryExercises
        val txtSearch = view.findViewById<EditText>(R.id.searchInput)

        val recyclerView: RecyclerView = view.findViewById(R.id.exercisesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        txtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                displayedExerciseList = exerciseService.searchExercises(s.toString(), fullListOfCategoryExercises)

            }

            override fun afterTextChanged(s: Editable?) {
                recyclerView.adapter = ExerciseAdapter(displayedExerciseList, requireContext(), object : ExerciseAdapter.OnItemClickListener {
                    override fun onItemClick(exercise: Exercise) {
                        RoutineBuilder.addExercise(exercise)
                        val fragmentManager = parentFragmentManager
                        fragmentManager.beginTransaction().apply {
                            replace(R.id.chooseCategoryFragmentContainer, EditExerciseFragment.newInstance(exercise))
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
                )
            }
        })


        recyclerView.adapter = ExerciseAdapter(displayedExerciseList, requireContext(), object : ExerciseAdapter.OnItemClickListener {
            override fun onItemClick(exercise: Exercise) {
                RoutineBuilder.addExercise(exercise)
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.chooseCategoryFragmentContainer, EditExerciseFragment.newInstance(exercise))
                    addToBackStack(null)
                    commit()
                }
            }
        }
        )
        return view
    }

    companion object {
        private const val ARG_CATEGORY_ID = "categoryId"

        @JvmStatic
        fun newInstance(categoryId: String) =
            AddExerciseListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId)

                }
            }
    }
}