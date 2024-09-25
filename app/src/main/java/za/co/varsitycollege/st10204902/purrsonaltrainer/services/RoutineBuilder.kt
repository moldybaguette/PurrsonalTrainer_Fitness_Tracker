package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

object RoutineBuilder {
    var routineID: String = CreateID.GenerateID()
    var name: String = ""
    var description: String = ""
    var exercises: MutableMap<String, Exercise> = mutableMapOf()
    var color: String = ""

    fun setRoutineName(name: String) {
        this.name = name
    }

    fun setRoutineColor(color: String) {
        this.color = color
    }

    fun setRoutineDescription(description: String) {
        this.description = description
    }

    fun hasAnExercise(): Boolean {
        return exercises.isNotEmpty()
    }

    fun addExercise(exercise: Exercise) {
        // if the exercise is not already in the exercises list then add it
        if (!exercises.contains(exercise.exerciseID)) {
            exercises[exercise.exerciseID] = exercise
        }
    }

    fun buildRoutine(): UserRoutine {
        val newRoutine =  UserRoutine(routineID, name , color, description, exercises)
        routineID= CreateID.GenerateID()
        name = ""
        description = ""
        exercises = mutableMapOf()
        color = ""
        return newRoutine
    }
}