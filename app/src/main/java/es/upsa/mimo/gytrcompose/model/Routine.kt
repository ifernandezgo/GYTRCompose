package es.upsa.mimo.gytrcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "routine")
data class Routine(
    @PrimaryKey(autoGenerate = true) val routineId: Int? = null,
    var name: String = ""
)