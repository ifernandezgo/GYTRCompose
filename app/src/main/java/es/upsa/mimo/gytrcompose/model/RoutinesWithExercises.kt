package es.upsa.mimo.gytrcompose.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RoutinesWithExercises(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val exercises: List<Exercise>
)
