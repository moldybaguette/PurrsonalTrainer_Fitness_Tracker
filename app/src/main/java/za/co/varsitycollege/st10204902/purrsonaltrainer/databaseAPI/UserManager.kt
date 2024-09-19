package za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Item
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

object UserManager {
    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow.asStateFlow()

    private var userSyncJob: Job? = null

    fun setUser(user: User) {
        _userFlow.value = user
        println(user.userID)
        startUserSync()
    }

    fun clearUser() {
        _userFlow.value = null
    }

    fun loginUser(userId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    setUser(user)
                    println("User loaded from Firebase and set in UserManager")
                    println("THE USERS NAME IS: ${user.name}")
                    println("THE USERS CAT NAME IS: ${user.catName}")
                    println("THE USERS ID IS: ${user.userID}")
                } else {
                    // Handle user not found
                    println("User not found in Firebase")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                println("Database error: ${databaseError.message}")
            }
        })
    }

    private fun startUserSync() {
        userSyncJob?.cancel()
        userSyncJob = CoroutineScope(Dispatchers.IO).launch {
            _userFlow
                .filterNotNull()
                .debounce(500) // Debounce to prevent too frequent updates
                .collectLatest { user ->
                    println("UserManager: Detected user change, synchronizing with Firebase")
                    synchronizeUserWithFirebase(user)
                }
        }
    }

    private fun synchronizeUserWithFirebase(user: User) {
        val userId = user.userID
        if (userId.isNotEmpty()) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseRef.setValue(user)
                .addOnSuccessListener {
                    // Data successfully written
                    println("UserManager: User data synchronized with Firebase")
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    println("UserManager: Failed to synchronize user data: ${exception.message}")
                }
        } else {
            println("UserManager: User ID is empty, cannot synchronize")
        }
    }

// Update User Properties

    fun updateUserName(newName: String) {
        _userFlow.value = _userFlow.value?.copy(name = newName)
    }

    fun updateExperiencePoints(newPoints: String) {
        _userFlow.value = _userFlow.value?.copy(experiencePoints = newPoints)
    }

    fun updateCatName(newCatName: String) {
        _userFlow.value = _userFlow.value?.copy(catName = newCatName)
    }

    // adding & removing routines
    fun addUserRoutine(newRoutine: UserRoutine) {
        _userFlow.value?.let { user ->
            val updatedRoutines = user.userRoutines + (newRoutine.routineID to newRoutine)
            _userFlow.value = user.copy(userRoutines = updatedRoutines)
        }
    }

    fun removeUserRoutine(routineID: String) {
        _userFlow.value?.let { user ->
            val updatedRoutines = user.userRoutines - routineID
            _userFlow.value = user.copy(userRoutines = updatedRoutines)
        }
    }
    // adding & removing exercises

    fun addUserExercise(newExercise: Exercise) {
        _userFlow.value?.let { user ->
            val updatedExercises = user.userExercises + (newExercise.exerciseID to newExercise)
            _userFlow.value = user.copy(userExercises = updatedExercises)
        }
    }

    fun removeUserExercise(exerciseID: String) {
        _userFlow.value?.let { user ->
            val updatedExercises = user.userExercises - exerciseID
            _userFlow.value = user.copy(userExercises = updatedExercises)
        }
    }

    fun addItemToInventory(item: Item) {
        _userFlow.value?.let { user ->
            val updatedInventory = user.userInventory + (item.itemID to item)
            _userFlow.value = user.copy(userInventory = updatedInventory)
        }
    }
}