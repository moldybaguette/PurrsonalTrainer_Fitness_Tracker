package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CategoryAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.ExerciseAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseService

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
        var exercises = exerciseService.getExerciseInCategory(categoryId!!)

        val recyclerView: RecyclerView = view.findViewById(R.id.categoryRecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = ExerciseAdapter(exercises, requireContext(), object : ExerciseAdapter.OnItemClickListener {
            override fun onItemClick(exercise: Exercise) {
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.chooseCategoryFragmentContainer, EditExerciseFragment.newInstance(exercise))
                    addToBackStack(null)
                    commit()
                }
            }
        })
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