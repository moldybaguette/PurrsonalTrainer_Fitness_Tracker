package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Intent
import android.os.Bundle
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoutinesFragment : Fragment(), OnRoutineItemClickListener {

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
        val view = inflater.inflate(R.layout.fragment_routines, container, false)

        // Routines adapter setup
        val routinesList = view.findViewById<RecyclerView>(R.id.routinesRecyclerView)
        routinesList.layoutManager = LinearLayoutManager(requireContext())
        routinesList.adapter = RoutineListAdapter(UserManager.user!!.userRoutines.values.toList(), requireContext(), this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val makeNewRoutineButton: ImageButton = view.findViewById(R.id.add_button)
        makeNewRoutineButton.setOnClickListener {
            val intent = Intent(requireActivity(), CreateRoutineActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoutinesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(routine: UserRoutine)
    {
        TODO("Not yet implemented")
    }
}