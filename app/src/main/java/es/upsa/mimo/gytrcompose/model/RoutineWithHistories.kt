package es.upsa.mimo.gytrcompose.model

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithHistories(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val histories: List<History>
)