package za.co.varsitycollege.st10204902.purrsonaltrainer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import za.co.valsitycollege.st10204902.purrsonaltrainer.models.*
import za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI.UserManager.loginUser
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
//------------------------***EOF***-----------------------------//

