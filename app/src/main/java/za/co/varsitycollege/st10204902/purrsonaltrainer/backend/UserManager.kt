package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Item
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserAchievement
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserBackground
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise


object UserManager {

    //                          PROPERTIES                       //
    //-----------------------------------------------------------//

    /**
     * The path to the users in the Firebase Realtime Database
     */
    private const val USERS_PATH = "users"

    /**
     * A MutableStateFlow that holds the current user
     */
    private val _userFlow = MutableStateFlow<User?>(null)


    /**
     * A read-only property that exposes the current user
     */
    val user: User?
        get() = _userFlow.value

    /**
     * a Job that is used to synchronize the user data with Firebase
     */
    private var userSyncJob: Job? = null

    /**
     * A CoroutineScope that is used to manage the user synchronization job
     */
    val userManagerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//


    // Authentication Methods
    //-----------------------------------------------------------//

    /**
     * Sets up the user singleton with the provided user ID
     * @param userId The ID of the user to set up the singleton with
     * @return A Result object containing the User if successful, or an exception if unsuccessful
     */
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

    /**
     * Logs out the current user
     */
    fun logoutUser() {
        clearUser()
        userSyncJob?.cancel()
        Log.i("UserManager", "User logged out")
    }

    /**
     * Checks if a user is logged in
     * @return True if the user is logged in, false if not
     */
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

    suspend fun deleteUserDataExceptID(){
        val currentUser = _userFlow.value ?: return
        val userId = currentUser.userID

        val updatedUser = User(
            userID = userId,
            name = "",
            catName = "",
            experiencePoints = "",
            backgroundURI = "",
            catURI = "",
            milkCoins = "",
            userRoutines = emptyMap(),
            userWorkouts = emptyMap(),
            userExercises = emptyMap(),
            userAchievements = emptyMap(),
            userInventory = emptyMap(),
            userBackgrounds = emptyMap()
        )

        try {
            val databaseRef = FirebaseDatabase.getInstance().getReference(USERS_PATH).child(userId)
            databaseRef.setValue(updatedUser).await()
            setUser(updatedUser)
            Log.d("UserManager", "User data cleared except for userID")
        } catch (e: Exception) {
            Log.e("UserManager", "Failed to clear user data: ${e.message}")
        }
    }

    /**
     * Updates the user's name
     * @param newName The new name to update the user with
     */
    fun updateUserName(newName: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(name = newName)
            }
            Log.d("UserManager", "User name updated to $newName")
        } else {
            Log.e("UserManager", "User is not logged in")
        }
    }

    /**
     * Updates the user's cat name
     * @param newCatName The new cat name to update the user with
     */
    fun updateCatName(newCatName: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(catName = newCatName)
            }
        }
    }

    /**
     * Updates the user's experience points
     * @param newPoints The new experience points to update the user with
     */
    fun updateExperiencePoints(newPoints: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(experiencePoints = newPoints)
            }
        }
    }

    /**
     * Updates the user's background URI
     * @param newURI The new background URI to update the user with
     */
    fun updateBackgroundURI(newURI: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(backgroundURI = newURI)
            }
        }
    }

    /**
     * Updates the user's cat URI
     * @param newURI The new cat URI to update the user with
     */
    fun updateCatURI(newURI: String) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(catURI = newURI)
            }
        }
    }

    /**
     * Updates the user's milk coins
     * @param newCoins The new milk coins to update the user with
     */
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

    /**
     * Adds a new user routine to the user
     * @param newRoutine The new routine to add to the user
     */
    fun addUserRoutine(newRoutine: UserRoutine) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(userRoutines = user.userRoutines + (newRoutine.routineID to newRoutine))
            }
        }
    }

    /**
     * Removes a user routine from the user
     * @param routineID The ID of the routine to remove
     */
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

    /**
     * Updates a user routine
     * @param routineID The ID of the routine to update
     * @param updatedRoutine The updated routine to replace the old one
     */
    fun updateUserRoutine(routineID: String, updatedRoutine: UserRoutine) {
        if (userIsLoggedIn()) {
            val newRoutine = updatedRoutine.copy(routineID = routineID)
            removeUserRoutine(routineID)
            addUserRoutine(newRoutine)
        }
    }
    //-----------------------------------------------------------//
    // UserWorkout Management
    //-----------------------------------------------------------//

    /**
     * Adds a new user workout to the user
     * @param newWorkout The new workout to add to the user
     */
    fun addUserWorkout(newWorkout: UserWorkout) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.copy(userWorkouts = user.userWorkouts + (newWorkout.workoutID to newWorkout))
            }
        }
    }

    /**
     * Removes a user workout from the user
     * @param workoutID The ID of the workout to remove
     */
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

    /**
     * Updates a user workout
     * @param workoutID The ID of the workout to update
     * @param updatedWorkout The updated workout to replace the old one
     */
    fun updateUserWorkout(workoutID: String, updatedWorkout: UserWorkout) {
        if (userIsLoggedIn()) {
            val newWorkout = updatedWorkout.copy(workoutID = workoutID)
            removeUserWorkout(workoutID)
            addUserWorkout(newWorkout)
        }
    }

    //-----------------------------------------------------------//
    // WorkoutExercise Management
    //-----------------------------------------------------------//

    /**
     * Adds an exercise to a workout
     * @param workoutID The ID of the workout to add the exercise to
     * @param exercise The exercise to add to the workout
     */
    fun addExerciseToWorkout(workoutID: String, exercise: WorkoutExercise) {
        if (userIsLoggedIn()) {
            _userFlow.update { user ->
                user?.let {
                    val updatedWorkout = it.userWorkouts[workoutID]?.workoutExercises?.plus(
                        exercise.exerciseID to exercise
                    )
                    updatedWorkout?.let { exercises ->
                        it.copy(
                            userWorkouts = it.userWorkouts + (workoutID to it.userWorkouts[workoutID]!!.copy(
                                workoutExercises = exercises
                            ))
                        )
                    } ?: it // Return the original user if no update occurred
                }
            }
        }
    }

    /**
     * Removes an exercise from a workout
     * @param workoutID The ID of the workout to remove the exercise from
     * @param exerciseID The ID of the exercise to remove
     */
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
                val updatedWorkout = it.userWorkouts[workoutID]?.workoutExercises?.minus(exerciseID)
                updatedWorkout?.let { exercises ->
                    it.copy(
                        userWorkouts = it.userWorkouts + (workoutID to it.userWorkouts[workoutID]!!.copy(
                            workoutExercises = exercises
                        ))
                    )
                } ?: it // Return the original user if no update occurred
            }
        }
    }

    /**
     * Updates an exercise in a workout
     * @param workoutID The ID of the workout containing the exercise
     * @param exerciseID The ID of the exercise to update
     * @param updatedExercise The updated exercise to replace the old one
     */
    fun updateExerciseInWorkout(
        workoutID: String,
        exerciseID: String,
        updatedExercise: WorkoutExercise
    ) {
        if (userIsLoggedIn()) {
            val newExercise = updatedExercise.copy(exerciseID = exerciseID)
            removeExerciseFromWorkout(workoutID, exerciseID)
            addExerciseToWorkout(workoutID, newExercise)
        }
    }

    //-----------------------------------------------------------//
    // UserExercise Management
    //-----------------------------------------------------------//

    /**
     * Adds a new exercise to the user
     * @param newExercise The new exercise to add to the user
     */
    fun addUserExercise(newExercise: Exercise) {
        _userFlow.update { user ->
            user?.let {
                val updatedExercises = user.userExercises + (newExercise.exerciseID to newExercise)
                it.copy(userExercises = updatedExercises)
            }
        }
    }

    /**
     * Removes an exercise from the user
     * @param exerciseID The ID of the exercise to remove
     */
    fun removeUserExercise(exerciseID: String) {
        _userFlow.update { user ->
            val exercises = user?.userExercises
            if (exercises.isNullOrEmpty() || !exercises.containsKey(exerciseID)) {
                Log.w(
                    "UserManager", "user exercises are either empty or the exercise doesn't exist"
                )
                return@update user // Return the user unchanged
            }
            user.let {
                val updatedExercises = user.userExercises - (exerciseID)
                it.copy(userExercises = updatedExercises)
            }
        }
    }

    /**
     * Updates an exercise in the user
     * @param exerciseID The ID of the exercise to update
     * @param updatedExercise The updated exercise to replace the old one
     */
    fun updateUserExercise(exerciseID: String, updatedExercise: Exercise) {
        if (userIsLoggedIn()) {
            val newExercise = updatedExercise.copy(exerciseID = exerciseID)
            removeUserExercise(exerciseID)
            addUserExercise(newExercise)
        }
    }

    //-----------------------------------------------------------//
    // UserAchievement Management
    //-----------------------------------------------------------//

    /**
     * Adds a new achievement to the user
     * @param newAchievement The new achievement to add to the user
     */
    fun addUserAchievement(newAchievement: UserAchievement) {
        _userFlow.update { user ->
            user?.let {
                val updatedAchievements =
                    user.userAchievements + (newAchievement.achievementID to newAchievement)
                it.copy(userAchievements = updatedAchievements)
            }
        }
    }

    /**
     * Removes an achievement from the user
     * @param achievementID The ID of the achievement to remove
     */
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

    /**
     * Updates an achievement in the user
     * @param achievementID The ID of the achievement to update
     * @param updatedAchievement The updated achievement to replace the old one
     */
    fun updateUserAchievement(achievementID: String, updatedAchievement: UserAchievement) {
        if (userIsLoggedIn()) {
            val newAchievement = updatedAchievement.copy(achievementID = achievementID)
            removeUserAchievement(achievementID)
            addUserAchievement(newAchievement)
        }
    }

    //-----------------------------------------------------------//
    // UserInventory Management
    //-----------------------------------------------------------//

    /**
     * Adds an item to the user's inventory
     * @param newItem The new item to add to the user's inventory
     */
    fun addItemToInventory(newItem: Item) {
        _userFlow.update { user ->
            user?.let {
                val updatedInventory = user.userInventory + (newItem.itemID to newItem)
                it.copy(userInventory = updatedInventory)
            }
        }
    }

    /**
     * Removes an item from the user's inventory
     * @param itemID The ID of the item to remove
     */
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

    /**
     * Updates an item in the user's inventory
     * @param itemID The ID of the item to update
     * @param updatedItem The updated item to replace the old one
     */
    fun updateUserItem(itemID: String, updatedItem: Item) {
        if (userIsLoggedIn()) {
            val newItem = updatedItem.copy(itemID = itemID)
            removeItemFromInventory(itemID)
            addItemToInventory(newItem)
        }
    }

    //-----------------------------------------------------------//
    // UserBackground Management
    //-----------------------------------------------------------//

    /**
     * Adds a new background to the user
     * @param newBackground The new background to add to the user
     */
    fun addUserBackground(newBackground: UserBackground) {
        _userFlow.update { user ->
            user?.let {
                val updatedBackgrounds =
                    user.userBackgrounds + (newBackground.backgroundID to newBackground)
                it.copy(userBackgrounds = updatedBackgrounds)
            }
        }
    }

    /**
     * Removes a background from the user
     * @param backgroundID The ID of the background to remove
     */
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

    /**
     * Updates a background in the user
     * @param backgroundID The ID of the background to update
     * @param updatedBackground The updated background to replace the old one
     */
    fun updateUserBackground(backgroundID: String, updatedBackground: UserBackground) {
        if (userIsLoggedIn()) {
            val newBackground = updatedBackground.copy(backgroundID = backgroundID)
            removeUserBackground(backgroundID)
            addUserBackground(newBackground)
        }
    }

    //-----------------------------------------------------------//
    // Synchronization Methods
    //-----------------------------------------------------------//

    /**
     * Starts the user synchronization job
     * This job listens for changes to the user and synchronizes the user data with Firebase
     */
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

    /**
     * Synchronizes the user data with Firebase
     * @param user The user to synchronize
     * @return A Result object containing Unit if successful, or an exception if unsuccessful
     */
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

    /**
     * Sets the user singleton
     */
    private fun setUser(user: User) {
        println(user.userID)
        _userFlow.value = user
        startUserSync()
    }

    /**
     * Clears the user singleton
     */
    private fun clearUser() {
        _userFlow.value = null
    }

    //-----------------------------------------------------------//
}
//------------------------***EOF***-----------------------------//