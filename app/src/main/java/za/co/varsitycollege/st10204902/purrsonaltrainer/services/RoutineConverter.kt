package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise
import java.util.Date

/**
 * Class for converting routines
 */
class RoutineConverter {

    /**
     * Converts a UserRoutine to a UserWorkout
     * @param routine The UserRoutine to be converted
     * @return The converted UserWorkout
     */
    fun convertUserRoutineToUserWorkout(routine: UserRoutine): UserWorkout {
        //temporary vals to hold info to be passed to constructor
        val workoutID = CreateID.GenerateID()
        val name = routine.name
        val workoutExercises = routine.exercises
        val durationSeconds = 0
        val date = Date()
        val workout = UserWorkout(workoutID, name, workoutExercises, durationSeconds, date)
        return workout
    }
}
