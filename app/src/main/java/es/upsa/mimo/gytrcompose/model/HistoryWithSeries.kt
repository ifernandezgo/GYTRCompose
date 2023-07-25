package es.upsa.mimo.gytrcompose.model

import androidx.room.Embedded
import androidx.room.Relation

data class HistoryWithSeries(
    @Embedded val history: History,
    @Relation(
        parentColumn = "historyId",
        entityColumn = "historyId"
    )
    val series: List<Serie>
)