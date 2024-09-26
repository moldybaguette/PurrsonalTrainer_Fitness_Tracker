package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnRoutineItemClickListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.CreateRoutineActivity

/**
 * Fragment for displaying and managing user routines.
 */
class RoutinesFragment : Fragment(), OnRoutineItemClickListener {

    /**
     * Called to do initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_routines, container, false)

        try {
            if (UserManager.user != null && UserManager.user!!.userRoutines.isNotEmpty()) {
                // Routines adapter setup
                val routinesList = view.findViewById<RecyclerView>(R.id.routinesRecyclerView)
                routinesList.layoutManager = LinearLayoutManager(requireContext())
                routinesList.adapter = RoutineListAdapter(UserManager.user!!.userRoutines.values.toList(), requireContext(), this)
            }
        } catch (e: Exception) {
            Log.e("ERROR loading routines", e.toString())
        }
        return view
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val makeNewRoutineButton: ImageButton = view.findViewById(R.id.add_button)
        makeNewRoutineButton.setOnClickListener {
            val intent = Intent(requireActivity(), CreateRoutineActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoutinesFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoutinesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    /**
     * Called when a routine item is clicked.
     * @param routine The clicked routine.
     */
    override fun onItemClick(routine: UserRoutine) {
        TODO("Not yet implemented")
    }
}