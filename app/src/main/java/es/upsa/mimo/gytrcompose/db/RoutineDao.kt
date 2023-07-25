package es.upsa.mimo.gytrcompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.RoutineExerciseCrossRef

@Dao
interface RoutineDao {

    @Insert
    fun insertRoutine(routine: Routine) : Long

    @Query("SELECT * FROM routine WHERE routineId = :id")
    fun getRoutineById(id: Int) : Routine?

    @Query("SELECT * FROM routine WHERE name = :name")
    fun getRoutineByName(name: String): Routine?

    @Query("SELECT * FROM routine")
    fun getAllRoutines() : LiveData<List<Routine>>

    @Delete
    fun deleteRoutine(routine: Routine, routineExercises: List<RoutineExerciseCrossRef>?)

    @Update
    fun updateRoutine(routine: Routine)

    @Query("SELECT COUNT(*) FROM routine")
    fun getRoutinesCount(): Int
}