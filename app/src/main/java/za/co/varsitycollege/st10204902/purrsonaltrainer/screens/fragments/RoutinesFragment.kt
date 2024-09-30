package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnRoutineItemClickListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.CreateRoutineActivity

class RoutinesFragment : Fragment(), OnRoutineItemClickListener {

    private lateinit var routinesRecyclerView: RecyclerView
    private lateinit var topSection: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_routines, container, false)

        topSection = view.findViewById(R.id.topSection)
        routinesRecyclerView = view.findViewById(R.id.routinesRecyclerView)

        applyFloatUpAnimation(topSection)
        applyFloatUpAnimation(routinesRecyclerView)

        try {
            if (UserManager.user != null && UserManager.user!!.userRoutines.isNotEmpty()) {
                val userRoutines = UserManager.user!!.userRoutines.values.toMutableList()
                val routinesList = view.findViewById<RecyclerView>(R.id.routinesRecyclerView)
                routinesList.layoutManager = LinearLayoutManager(requireContext())
                val adapter = RoutineListAdapter(userRoutines, requireContext(), this)
                routinesList.adapter = adapter

                // Set up swipe-to-delete
                setupSwipeToDelete(routinesList)
            }
        } catch (e: Exception) {
            Log.e("ERROR loading routines", e.toString())
        }
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

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as RoutineListAdapter
                val position = viewHolder.adapterPosition
                val routine = adapter.getRoutineAt(position)

                // Remove the item from the adapter
                adapter.removeItem(position)

                // Remove the routine from UserManager
                UserManager.removeUserRoutine(routine.routineID)


            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoutinesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onItemClick(routine: UserRoutine) {
        TODO("Not yet implemented")
    }

    private fun applyFloatUpAnimation(view: View?) {
        view?.let {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.float_up)
            it.startAnimation(animation)
        }
    }
}