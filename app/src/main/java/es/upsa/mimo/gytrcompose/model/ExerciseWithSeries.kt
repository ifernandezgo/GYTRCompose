package es.upsa.mimo.gytrcompose.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithSeries(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val series: List<Serie>
)
