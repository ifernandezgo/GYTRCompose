package es.upsa.mimo.gytrcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey val exerciseId: String,
    val name: String,
    val target: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String
)