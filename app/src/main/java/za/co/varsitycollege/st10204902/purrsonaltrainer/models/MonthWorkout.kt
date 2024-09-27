package za.co.varsitycollege.st10204902.purrsonaltrainer.models

data class MonthWorkout(
    val month: String, // e.g., "August 2024"
    val workouts: List<UserWorkout>
)
