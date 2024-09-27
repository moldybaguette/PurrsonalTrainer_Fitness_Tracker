package za.co.varsitycollege.st10204902.purrsonaltrainer

import org.junit.Assert.assertEquals
import org.junit.Test
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.WorkoutWorker
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.*

class WorkoutWorkerTest {

    private val userWorkouts = mapOf(
        "workout1" to UserWorkout(
            workoutExercises = mapOf(
                "exercise1" to WorkoutExercise(
                    exerciseID = "exercise1",
                    sets = mapOf(
                        "set1" to WorkoutSet(reps = 10, weight = 50),
                        "set2" to WorkoutSet(reps = 8, weight = 60)
                    )
                ),
                "exercise2" to WorkoutExercise(
                    exerciseID = "exercise2",
                    sets = mapOf(
                        "set1" to WorkoutSet(reps = 12, weight = 40)
                    )
                )
            )
        ),
        "workout2" to UserWorkout(
            workoutExercises = mapOf(
                "exercise1" to WorkoutExercise(
                    exerciseID = "exercise1",
                    sets = mapOf(
                        "set1" to WorkoutSet(reps = 6, weight = 70)
                    )
                )
            )
        )
    )

    private val workoutWorker = WorkoutWorker(userWorkouts)

    @Test
    fun getMaxRepsPerExercise_returnsCorrectMaxReps() {
        assertEquals(10, workoutWorker.getMaxRepsPerExercise("exercise1"))
        assertEquals(12, workoutWorker.getMaxRepsPerExercise("exercise2"))
    }


    @Test
    fun getTotalSets_returnsCorrectTotalSets() {
        assertEquals(4, workoutWorker.getTotalSets())
    }

    @Test
    fun getTotalReps_returnsCorrectTotalReps() {
        assertEquals(36, workoutWorker.getTotalReps())
    }

    @Test
    fun getTotalWeight_returnsCorrectTotalWeight() {
        assertEquals(220, workoutWorker.getTotalWeight())
    }

    @Test
    fun getTotalSetsPerExercise_returnsCorrectTotalSets() {
        assertEquals(3, workoutWorker.getTotalSetsPerExercise("exercise1"))
        assertEquals(1, workoutWorker.getTotalSetsPerExercise("exercise2"))
    }

    @Test
    fun getTotalRepsPerExercise_returnsCorrectTotalReps() {
        assertEquals(24, workoutWorker.getTotalRepsPerExercise("exercise1"))
        assertEquals(12, workoutWorker.getTotalRepsPerExercise("exercise2"))
    }

    @Test
    fun getTotalWeightPerExercise_returnsCorrectTotalWeight() {
        assertEquals(180, workoutWorker.getTotalWeightPerExercise("exercise1"))
        assertEquals(40, workoutWorker.getTotalWeightPerExercise("exercise2"))
    }

    @Test
    fun getTotalSetsPerWorkout_returnsCorrectTotalSets() {
        assertEquals(3, workoutWorker.getTotalSetsPerWorkout("workout1"))
        assertEquals(1, workoutWorker.getTotalSetsPerWorkout("workout2"))
    }

    @Test
    fun getTotalRepsPerWorkout_returnsCorrectTotalReps() {
        assertEquals(30, workoutWorker.getTotalRepsPerWorkout("workout1"))
        assertEquals(6, workoutWorker.getTotalRepsPerWorkout("workout2"))
    }

    @Test
    fun getTotalWeightPerWorkout_returnsCorrectTotalWeight() {
        assertEquals(150, workoutWorker.getTotalWeightPerWorkout("workout1"))
        assertEquals(70, workoutWorker.getTotalWeightPerWorkout("workout2"))
    }

    @Test
    fun getTotalSetsPerWorkoutPerExercise_returnsCorrectTotalSets() {
        assertEquals(2, workoutWorker.getTotalSetsPerWorkoutPerExercise("workout1", "exercise1"))
        assertEquals(1, workoutWorker.getTotalSetsPerWorkoutPerExercise("workout1", "exercise2"))
    }

    @Test
    fun getTotalRepsPerWorkoutPerExercise_returnsCorrectTotalReps() {
        assertEquals(18, workoutWorker.getTotalRepsPerWorkoutPerExercise("workout1", "exercise1"))
        assertEquals(12, workoutWorker.getTotalRepsPerWorkoutPerExercise("workout1", "exercise2"))
    }

    @Test
    fun getTotalWeightPerWorkoutPerExercise_returnsCorrectTotalWeight() {
        assertEquals(110, workoutWorker.getTotalWeightPerWorkoutPerExercise("workout1", "exercise1"))
        assertEquals(40, workoutWorker.getTotalWeightPerWorkoutPerExercise("workout1", "exercise2"))
    }
}