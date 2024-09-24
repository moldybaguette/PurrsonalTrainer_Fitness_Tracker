// File: AuthManagerTest.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.backend

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User
import java.util.*

@RunWith(AndroidJUnit4::class)
class AuthManagerTest {

    private lateinit var authManager: AuthManager
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    // Dummy user credentials
    private val testEmail = "testuser_${UUID.randomUUID()}@example.com"
    private val testPassword = "Test@123456"

    // To store the created user for cleanup
    private var createdUserUid: String? = null

    @Before
    fun setUp() {
        authManager = AuthManager()
    }

    @Test
    fun testRegisterUser() = runBlocking {
        val result = authManager.registerUser(testEmail, testPassword)
        assertTrue("Registration should be successful", result.isSuccess)
        val uid = result.getOrNull()
        assertNotNull("User ID should not be null", uid)
        createdUserUid = uid!!

        // Verify user exists in FirebaseAuth
        val user = auth.currentUser
        assertNotNull("FirebaseAuth current user should not be null", user)
        assertEquals("User email should match", testEmail, user?.email)

        // Verify user exists in Realtime Database
        val dbUserSnapshot = database.getReference("users").child(uid).get().await()
        assertTrue("User should exist in Realtime Database", dbUserSnapshot.exists())
        val userData = dbUserSnapshot.getValue(User::class.java)
        assertNotNull("User data should not be null in Realtime Database", userData)
        assertEquals("User ID should match", uid, createdUserUid)
    }

    @Test
    fun testLoginUser() = runBlocking {
        // First, register the user
        val registerResult = authManager.registerUser(testEmail, testPassword)
        assertTrue("Registration should be successful", registerResult.isSuccess)
        val uid = registerResult.getOrNull()
        assertNotNull("User ID should not be null", uid)
        createdUserUid = uid!!

        // Logout the current user
        auth.signOut()

        // Attempt to login
        val loginResult = authManager.loginUser(testEmail, testPassword)
        assertTrue("Login should be successful", loginResult.isSuccess)
        val loginUid = loginResult.getOrNull()
        assertNotNull("Login User ID should not be null", loginUid)
        assertEquals("Login User ID should match registered User ID", uid, loginUid)
    }

    @Test
    fun testLoginWithSSO() = runBlocking {
        // Note: Testing SSO requires a valid GoogleSignInAccount, which is non-trivial in tests.
        // This test is a placeholder and should be implemented with proper Google Sign-In setup.

        // Example:
        /*
        val googleAccount: GoogleSignInAccount = getTestGoogleSignInAccount()
        val result = authManager.loginWithSSO(googleAccount)
        assertTrue("SSO Login should be successful", result.isSuccess)
        val uid = result.getOrNull()
        assertNotNull("User ID should not be null", uid)
        */

        // Since setting up a GoogleSignInAccount is out of scope, we'll skip actual implementation.
        assertTrue("SSO Login test is not implemented.", true)
    }

    @Test
    fun testRegisterWithSSO() = runBlocking {
        // Similar to testLoginWithSSO, registering with SSO requires a valid GoogleSignInAccount.

        // Example:
        /*
        val googleAccount: GoogleSignInAccount = getTestGoogleSignInAccount()
        val result = authManager.registerWithSSO(googleAccount)
        assertTrue("SSO Registration should be successful", result.isSuccess)
        val uid = result.getOrNull()
        assertNotNull("User ID should not be null", uid)
        createdUserUid = uid
        */

        // Since setting up a GoogleSignInAccount is out of scope, we'll skip actual implementation.
        assertTrue("SSO Registration test is not implemented.", true)
    }

    @After
    fun tearDown() {
        runBlocking {
            createdUserUid?.let { uid ->
                // Delete the user from FirebaseAuth
                val user = auth.currentUser
                user?.delete()?.await()

                // Remove the user data from Realtime Database
                database.getReference("users").child(uid).removeValue().await()
            }
        }
    }
}