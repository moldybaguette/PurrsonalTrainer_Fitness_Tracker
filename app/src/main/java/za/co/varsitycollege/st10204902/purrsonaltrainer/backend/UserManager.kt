package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.valsitycollege.st10204902.purrsonaltrainer.models.*


object UserManager {

    //                          PROPERTIES                       //
    //-----------------------------------------------------------//


    private const val USERS_PATH = "users"
    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow.asStateFlow()

    // Expose the current user as a read-only property
    val user: User?
        get() = _userFlow.value

    private var userSyncJob: Job? = null
    val userManagerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//


    // Authentication Methods
    //-----------------------------------------------------------//

    suspend fun setUpSingleton(userId: String): Result<User> {
        return try {
            val databaseRef = FirebaseDatabase.getInstance().getReference(USERS_PATH).child(userId)
            val dataSnapshot = databaseRef.get().await()
            val user = dataSnapshot.getValue(User::class.java)
                ?: throw Exception("User not found in Firebase")
            setUser(user)
            Log.d("UserManager", "User set with ID: ${user.userID}")
            Result.success(user)
        } catch (e: Exception) {
            Log.e("UserManager", "Failed to load user: ${e.message}")
            Result.failure(e)
        }
    }

    // not sure if this needs extra functionality
    fun logoutUser() {
        clearUser()
        userSyncJob?.cancel()
        Log.i("UserManager", "User logged out")
    }

    fun userIsLoggedIn(): Boolean {
        if (_userFlow.value == null) {
            Log.i("UserManager", "User is not logged in")
            return false
        }
        return _userFlow.value != null

    }

    //-----------------------------------------------------------//
    // User Property Updates
    //-----------------------------------------------------------//
    fun updateUserName(newName: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(name = newName)
            }
            Log.d("UserManager", "User name updated to $newName")
        }
        else {
            Log.e("UserManager", "User is not logged in")
        }
    }


    fun updateCatName(newCatName: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(catName = newCatName)
            }
        }
    }

    fun updateExperiencePoints(newPoints: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(experiencePoints = newPoints)
            }
        }
    }


    fun updateBackgroundURI(newURI: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(backgroundURI = newURI)
            }
        }
    }


    fun updateCatURI(newURI: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(catURI = newURI)
            }
        }
    }


    fun updateMilkCoins(newCoins: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(milkCoins = newCoins)
            }
        }
    }

    //-----------------------------------------------------------//
    // UserRoutine Management
    //-----------------------------------------------------------//

    fun addUserRoutine(newRoutine: UserRoutine) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(userRoutines = user.userRoutines + (newRoutine.routineID to newRoutine))
            }
        }
    }


    fun removeUserRoutine(routineID: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                if (user?.userRoutines.isNullOrEmpty()) {
                    Log.w("UserManager", "User routines are empty or null")
                    return@update user // Return the user unchanged
                }
                user!!.copy(userRoutines = user.userRoutines - (routineID))
            }
        }
    }
//todo make update methods
    //-----------------------------------------------------------//
    // UserWorkout Management
    //-----------------------------------------------------------//

    fun addUserWorkout(newWorkout: UserWorkout) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(userWorkouts = user.userWorkouts + (newWorkout.workoutID to newWorkout))
            }
        }
    }

    fun removeUserWorkout(workoutID: String) {

        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                if (user?.userWorkouts.isNullOrEmpty()) {
                    Log.w("UserManager", "User Workouts are empty or null")
                    return@update user // Return the user unchanged
                }
                user?.copy(userWorkouts = user.userWorkouts - (workoutID))
            }
        }
    }

    //-----------------------------------------------------------//
    // WorkoutExercise Management
    //-----------------------------------------------------------//
    fun addExerciseToWorkout(workoutID: String, exercise: WorkoutExercise) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.let {
                    val updatedWorkout = it.userWorkouts[workoutID]?.workoutExercises?.plus(
                        exercise.exerciseID to exercise
                    )
                    updatedWorkout?.let { exercises ->
                        it.copy(
                            userWorkouts = it.userWorkouts + (
                                workoutID to it.userWorkouts[workoutID]!!.copy(
                                    workoutExercises = exercises
                                )
                            )
                        )
                    } ?: it // Return the original user if no update occurred
                }
            }
        }
    }


    fun removeExerciseFromWorkout(workoutID: String, exerciseID: String) {
        _userFlow.update { user ->
            val exercises = user?.userWorkouts?.get(workoutID)?.workoutExercises
            if (exercises.isNullOrEmpty() || !exercises.containsKey(exerciseID)) {
                Log.w(
                    "UserManager",
                    "the workout or doesn't exist or itn doesn't contain the exercise"
                )
                return@update user // Return the user unchanged
            }

            user.let {
                val updatedWorkout =
                    it.userWorkouts[workoutID]?.workoutExercises?.minus(exerciseID)
                updatedWorkout?.let { exercises ->
                    it.copy(
                        userWorkouts = it.userWorkouts + (
                            workoutID to it.userWorkouts[workoutID]!!.copy(
                                workoutExercises = exercises
                            )
                        )
                    )
                } ?: it // Return the original user if no update occurred
            }
        }
    }

    //-----------------------------------------------------------//
    // UserExercise Management
    //-----------------------------------------------------------//
    fun addUserExercise(newExercise: Exercise) {
        _userFlow.update { user ->
            user?.let {
                val updatedExercises = user.userExercises + (newExercise.exerciseID to newExercise)
                it.copy(userExercises = updatedExercises)
            }
        }
    }

    fun removeUserExercise(exerciseID: String) {
        _userFlow.update { user ->
            val exercises = user?.userExercises
            if (exercises.isNullOrEmpty() || !exercises.containsKey(exerciseID)) {
                Log.w(
                    "UserManager",
                    "user exercises are either empty or the exercise doesn't exist"
                )
                return@update user // Return the user unchanged
            }
            user.let {
                val updatedExercises = user.userExercises - (exerciseID)
                it.copy(userExercises = updatedExercises)
            }
        }
    }

    //-----------------------------------------------------------//
    // UserAchievement Management
    //-----------------------------------------------------------//
    fun addUserAchievement(newAchievement: UserAchievement) {
        _userFlow.update { user ->
            user?.let {
                val updatedAchievements =
                    user.userAchievements + (newAchievement.achievementID to newAchievement)
                it.copy(userAchievements = updatedAchievements)
            }
        }
    }

    fun removeUserAchievement(achievementID: String) {
        _userFlow.update { user ->
            val achievements = user?.userAchievements
            if (achievements.isNullOrEmpty() || !achievements.containsKey(achievementID)) {
                Log.w(
                    "UserManager",
                    "User Achievement are empty or null or the achievement doesn't exist"
                )
                return@update user // Return the user unchanged
            }
            user.let {
                val updatedAchievements =
                    user.userAchievements + (achievementID to user.userAchievements[achievementID]!!)
                it.copy(userAchievements = updatedAchievements)
            }
        }
    }

    //-----------------------------------------------------------//
    // UserInventory Management
    //-----------------------------------------------------------//
    fun addItemToInventory(newItem: Item) {
        _userFlow.update { user ->
            user?.let {
                val updatedInventory = user.userInventory + (newItem.itemID to newItem)
                it.copy(userInventory = updatedInventory)
            }
        }
    }

    fun removeItemFromInventory(itemID: String) {
        _userFlow.update { user ->
            val inventory = user?.userInventory
            if (inventory.isNullOrEmpty() || !inventory.containsKey(itemID)) {
                Log.w("UserManager", "User Inventory is empty or the item doesn't exist")
                return@update user // Return the user unchanged
            }
            user.let {
                val updatedInventory = user.userInventory - (itemID)
                it.copy(userInventory = updatedInventory)
            }
        }
    }

    //-----------------------------------------------------------//
    // UserBackground Management
    //-----------------------------------------------------------//
    fun addUserBackground(newBackground: UserBackground) {
        _userFlow.update { user ->
            user?.let {
                val updatedBackgrounds =
                    user.userBackgrounds + (newBackground.backgroundID to newBackground)
                it.copy(userBackgrounds = updatedBackgrounds)
            }
        }
    }

    fun removeUserBackground(backgroundID: String) {
        _userFlow.update { user ->
            val backgrounds = user?.userBackgrounds
            if (backgrounds.isNullOrEmpty() || !backgrounds.containsKey(backgroundID)) {
                Log.w("UserManager", "User Backgrounds are empty or the background doesn't exist")
                return@update user // Return the user unchanged
            }
            user.let {
                val updatedBackgrounds = user.userBackgrounds - (backgroundID)
                it.copy(userBackgrounds = updatedBackgrounds)
            }
        }
    }

    //-----------------------------------------------------------//
    // Synchronization Methods
    //-----------------------------------------------------------//

    private fun startUserSync() {
        userSyncJob?.cancel()
        userSyncJob = userManagerScope.launch {
            _userFlow.filterNotNull().debounce(300).collectLatest { user ->
                Log.d("UserManager", "Detected user change, synchronizing with Firebase")
                val result = synchronizeUserWithFirebase(user)
                result.onFailure { exception ->
                    Log.e("UserManager", "Failed to synchronize user data: ${exception.message}")
                }

            }
        }
    }

    private suspend fun synchronizeUserWithFirebase(user: User): Result<Unit> {
        return try {
            val userId = user.userID
            if (userId.isNotEmpty()) {
                val databaseRef =
                    FirebaseDatabase.getInstance().getReference(USERS_PATH).child(userId)
                databaseRef.setValue(user).await()
                Log.d("UserManager", "User data synchronized with Firebase")
                Result.success(Unit)
            } else {
                val exception = Exception("User ID is empty, cannot synchronize")
                Log.e("UserManager", exception.message ?: "")
                Result.failure(exception)
            }
        } catch (e: Exception) {
            Log.e("UserManager", "Failed to synchronize user data: ${e.message}")
            Result.failure(e)
        }
    }

    //-----------------------------------------------------------//
    // Private helper methods
    //-----------------------------------------------------------//

    private fun setUser(user: User) {
        println(user.userID)
        _userFlow.value = user
        startUserSync()
    }


    private fun clearUser() {
        _userFlow.value = null
    }
    //-----------------------------------------------------------//
}
//------------------------***EOF***-----------------------------//