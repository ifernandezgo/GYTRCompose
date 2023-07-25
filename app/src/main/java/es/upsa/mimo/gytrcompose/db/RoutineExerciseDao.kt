package es.upsa.mimo.gytrcompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.upsa.mimo.gytrcompose.model.RoutineExerciseCrossRef

@Dao
interface RoutineExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertRoutineWithExercise(routineExercise: RoutineExerciseCrossRef)

    @Query("SELECT exerciseId FROM RoutineExerciseCrossRef WHERE routineId = :routineId")
    fun getRoutineExercises(routineId: Int): LiveData<List<String>?>

    @Query("SELECT * FROM RoutineExerciseCrossRef WHERE routineId = :routineId")
    fun getExercisesByRoutineId(routineId: Int): LiveData<List<RoutineExerciseCrossRef>?>

    @Delete
    fun deleteExerciseFromRoutine(routineExercise: RoutineExerciseCrossRef)
}