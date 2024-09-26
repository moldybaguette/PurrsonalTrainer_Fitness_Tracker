package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet

object SetBuilder {
    var workoutSetID: String = CreateID.GenerateID()
    var weight: Int? = 0
    var reps: Int? = 0
    var distance: Int? = 0
    var durationSeconds: Int? = 0
    var setType: String = ""

    fun pleaseSetWorkoutSetID(workoutSetID: String) {
        this.workoutSetID = workoutSetID
    }

    fun setWeight(weight: Int) {
        this.weight = weight
    }

    fun setReps(reps: Int) {
        this.reps = reps
    }

    fun setDistance(distance: Int) {
        this.distance = distance
    }

    fun setDurationSeconds(durationSeconds: Int) {
        this.durationSeconds = durationSeconds
    }

    fun pleaseSetSetType(setTypeParam: String) {
        this.setType = setTypeParam
    }

    fun build(): WorkoutSet {
        val workoutSet = WorkoutSet(
            workoutSetID,
            weight,
            reps,
            distance,
            durationSeconds,
            setType
        )
        workoutSetID = CreateID.GenerateID()
        weight = 0
        reps = 0
        distance = 0
        durationSeconds = 0
        setType = ""
        return workoutSet
    }
}
