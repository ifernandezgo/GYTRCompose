package es.upsa.mimo.gytrcompose.model

import androidx.room.Entity

@Entity(primaryKeys = ["exerciseId", "routineId"])
data class RoutineExerciseCrossRef(
    val exerciseId: String,
    val routineId: Int
)