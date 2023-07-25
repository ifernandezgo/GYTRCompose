package es.upsa.mimo.gytrcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "serie")
data class Serie(
    @PrimaryKey(autoGenerate = true) val serieId: Int? = null,
    val repetitions: Int,
    val weight: Float,
    val exerciseId: String,
    var historyId: Int? = null,
    val date: Long = Date().time
)