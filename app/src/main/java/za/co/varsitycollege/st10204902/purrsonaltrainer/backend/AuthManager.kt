package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User

class AuthManager(val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    //                          PROPERTIES                       //
    //----------------------------------------------------------//

    /**
     * The Firebase Realtime Database instance
     */
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    //----------------------------------------------------------//
    //                          METHODS                          //
    //----------------------------------------------------------//

    /**
     * Registers a user with the provided email and password
     * USE authResult.user.uid to get the user's unique ID to setup the singleton in UserManger
     * @param email The email of the user
     * @param password The password of the user
     * @return A Result object containing the Users ID if successful, or an exception if unsuccessful
     */
    suspend fun registerUser(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userID = createUserInRealtimeDatabase(authResult.user!!.uid)
            Result.success(userID)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logs in a user with the provided email and password
     * @param email The email of the user
     * @param password The password of the user
     * @return A Result object containing the Users ID if successful, or an exception if unsuccessful
     */
    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * creates the user in the realtime database when they register
     * @param userID The user's unique ID
     * @return The user's unique ID
     */
    fun createUserInRealtimeDatabase(userID: String): String {

        val userObject = User(
            userID,
            "",
            "",
            "",
            "",
            "",
            "",
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap(),
            emptyMap()
        )
        UserManager.userManagerScope.launch {
            val task =
                database.reference.child("users").child(userID).setValue(userObject)
            task.addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    println("User added to Realtime Database successfully")
                } else {
                    println("Failed to add user to Realtime Database: ${task.exception?.message}")
                }
            }
        }
        return userObject.userID
    }
//--------------------------------------------------------------//
}
//------------------------***EOF***-----------------------------//