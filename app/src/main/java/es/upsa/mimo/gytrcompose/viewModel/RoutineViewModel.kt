package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.RoutineExerciseCrossRef

class RoutineViewModel(application: Application) : AndroidViewModel(application){

    private var repository: DbRepository = DbRepository(application)

    suspend fun getRoutineById(routineId: Int): Routine? = repository.getRoutineById(routineId)

    fun getRoutineExercises(routineId: Int): LiveData<List<String>?> = repository.getRoutineExercises(routineId)

    suspend fun getExercisesById(exerciseId: List<String>): List<Exercise>? = repository.getExercisesByIds(exerciseId)

    fun getExercisesByRoutineId(routineId: Int): LiveData<List<RoutineExerciseCrossRef>?> = repository.getExercisesByRoutineId(routineId)

    suspend fun deleteRoutine(routine: Routine, routineExercises: List<RoutineExerciseCrossRef>?) = repository.deleteRoutine(routine, routineExercises)

}