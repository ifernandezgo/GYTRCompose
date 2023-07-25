package es.upsa.mimo.gytrcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val historyId: Int,
    val date: Long = Date().time,
    val time: Int,
    val routineId: Int
)