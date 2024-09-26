package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet
import java.util.Date

object RoutineBuilder {
    //-----------------------------------------------------------//
    //                          PROPERTIES                       //
    //-----------------------------------------------------------//
    /**
     * The routineID of the routine being built
     */
    var routineID: String = CreateID.GenerateID()

    /**
     * The name of the routine being built
     */
    var name: String = ""

    /**
     * The description of the routine being built
     */
    var description: String = ""

    /**
     * The exercises in the routine being built
     */
    var exercises: MutableMap<String, WorkoutExercise> = mutableMapOf()

    /**
     * The color of the routine being built
     */
    var color: String = ""

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    /**
     * Sets the name of the routine being built
     * @param name The name of the routine
     */
    fun setRoutineName(name: String) {
        this.name = name
    }

    /**
     * Sets the color of the routine being built
     * @param color The color of the routine
     */
    fun setRoutineColor(color: String) {
        this.color = color
    }

    /**
     * Sets the description of the routine being built
     * @param description The description of the routine
     */
    fun setRoutineDescription(description: String) {
        this.description = description
    }

    /**
     * Checks if the routine has an exercise
     * @return Boolean - True if the routine has an exercise, false if not
     */
    fun hasAnExercise(): Boolean {
        return exercises.isNotEmpty()
    }

    /**
     * Adds an exercise to the routine being built
     * @param exercise The exercise to add
     */
    fun addExercise(exercise: Exercise) {
        // if the exercise is not already in the exercises list then add it
        if (!exercises.contains(exercise.exerciseID)) {
            val tempExerciseID = exercise.exerciseID
            val tempExerciseName = exercise.exerciseName
            val tempCategory = exercise.category
            val tempNotes = exercise.notes
            val tempSets = mapOf<String, WorkoutSet>()
            exercises[tempExerciseID] = WorkoutExercise(tempExerciseID, tempExerciseName, tempCategory, tempSets , Date(), tempNotes)
        }
    }

    /**
     * Builds the routine and resets the builder
     * @return UserRoutine - The routine that was built
     */
    fun buildRoutine(): UserRoutine {
        val newRoutine =  UserRoutine(routineID, name , color, description, exercises)
        routineID= CreateID.GenerateID()
        name = ""
        description = ""
        exercises = mutableMapOf()
        color = ""
        return newRoutine
    }
    //-----------------------------------------------------------//
}
//------------------------***EOF***-----------------------------//