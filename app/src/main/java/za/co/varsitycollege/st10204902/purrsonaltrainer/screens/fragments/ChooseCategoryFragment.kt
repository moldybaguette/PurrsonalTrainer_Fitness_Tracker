package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CategoryAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseService
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.SlideUpPopup
import java.io.InputStreamReader
import java.lang.Thread.sleep

class ChooseCategoryFragment() : Fragment() {

    private lateinit var categories: MutableList<String>
    private val usersCustomExercises: List<String>?
        get() = UserManager.user?.customCategories

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_choose_category, container, false)

        var exerciseService = ExerciseService(requireContext())
        categories = exerciseService.defaultCategories.toMutableList()

        // Add all the unique exercise categories from the user's custom exercises that don't already exist in the default exercises
        val completeCategoryList = usersCustomExercises?.let {
            addUsersCustomCategories(categories, it)
        } ?: categories

            val recyclerView: RecyclerView = view.findViewById(R.id.categoryRecycler)
            recyclerView.layoutManager = LinearLayoutManager(context)
            //print each category
            Log.d("ChooseCategoryFragment", "completeCategoryList: ${completeCategoryList.listIterator()}")
            recyclerView.adapter = CategoryAdapter(completeCategoryList, requireContext(), object : CategoryAdapter.OnItemClickListener {
                override fun onItemClick(category: String) {
                    val fragmentManager = parentFragmentManager
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.chooseCategoryFragmentContainer, AddExerciseListFragment.newInstance(category))
                        addToBackStack(null)
                        commit()
                    }
                }
            })

        // Add category navigation code
        setupAddCategoryPopup(view)

        return view
    }

        private fun addUsersCustomCategories(mainCategoryList: MutableList<String>, customCategories: List<String>): List<String> {
            customCategories.forEach {
                if (!mainCategoryList.contains(it)) {
                    mainCategoryList.add(it)
                }
            }
        return mainCategoryList
    }

    private suspend fun loadExercisesMappedByCategory(jsonFileName: String): List<String> {
        return CoroutineScope(Dispatchers.IO).async {
            val gson = Gson()
            val assetManager = requireContext().assets
            val inputStream = assetManager.open(jsonFileName)
            val reader = InputStreamReader(inputStream)
            val exerciseListType = object : TypeToken<List<Exercise>>() {}.type
            val exercises: List<Exercise> = gson.fromJson(reader, exerciseListType)
            reader.close()
            exercises.map { it.category }.distinct()
        }.await()
    }

    // Example of how to use the SlideUpPopup class
    private fun setupAddCategoryPopup(view: View)
    {
        val addCategoryButton = view.findViewById<LinearLayout>(R.id.addCategoryButton)
        val fragmentContainer = requireActivity().findViewById<FrameLayout>(R.id.createCategoryFragmentContainer)
        val dismissArea = requireActivity().findViewById<View>(R.id.createCategoryDismissArea)

        // setup popup
        val fragment = CreateCategoryFragment()
        val popup = SlideUpPopup(parentFragmentManager, fragmentContainer, dismissArea, fragment, requireContext())
        addCategoryButton.setOnClickListener { popup.showPopup() }

        // Setting dismiss action so that fragment is removed when user enters category
        fragment.setDismissAction { popup.dismissPopup() }
    }
}