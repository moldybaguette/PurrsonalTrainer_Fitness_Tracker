package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import za.co.valsitycollege.st10204902.purrsonaltrainer.models.User

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
            val userID = createUserInRealtimeDatabase(authResult)
            Result.success(userID)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(authResult.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createUserInRealtimeDatabase(authUser: AuthResult): String {
        val userObject = User(authUser.user!!.uid, "", "", "", "", "", "", emptyMap(), emptyMap(), emptyMap(), emptyMap(), emptyMap(), emptyMap())
        FirebaseDatabase.getInstance().reference.child("users").child(authUser.user!!.uid).setValue(userObject)
        return authUser.user!!.uid
    }



}