package es.upsa.mimo.gytrcompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.upsa.mimo.gytrcompose.model.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise WHERE exerciseId = :id")
    fun getExerciseById(id: String) : Exercise?

    @Query("SELECT * FROM exercise WHERE exerciseId In (:ids)")
    fun getExercisesByIds(ids: List<String>) : List<Exercise>?

    @Query("SELECT * FROM exercise")
    fun getAllExercises(): LiveData<List<Exercise>>
}