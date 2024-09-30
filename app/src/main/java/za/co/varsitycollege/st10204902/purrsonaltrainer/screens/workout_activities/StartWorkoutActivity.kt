package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnRoutineItemClickListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartWorkoutBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class StartWorkoutActivity : AppCompatActivity(), OnRoutineItemClickListener {
    private lateinit var binding: ActivityStartWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val makeNewRoutineButton: LinearLayout = findViewById(R.id.makeNewRoutineButton)
        makeNewRoutineButton.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }
        if (UserManager.user != null && UserManager.user!!.userRoutines.isNotEmpty()) {
            var savedWorkoutsDisplay = findViewById<RecyclerView>(R.id.savedWorkoutsDisplay)
            val userRoutines = UserManager.user!!.userRoutines.values.toMutableList()
            savedWorkoutsDisplay.layoutManager = LinearLayoutManager(this)
            val adapter = RoutineListAdapter(userRoutines, this, this)
            savedWorkoutsDisplay.adapter = adapter
        }

        // Navigation to StartEmptyWorkoutActivity
        binding.startEmptyWorkoutButton.setOnClickListener {
            navigateTo(this, StartEmptyWorkoutActivity::class.java, null)
        }
    }

    override fun onItemClick(routine: UserRoutine) {
        // Adding routineID for which the workout will be created
        val bundle = Bundle()
        bundle.putString("routineID", routine.routineID)
        navigateTo(this, MadeRoutineActivity::class.java, bundle)
    }
}