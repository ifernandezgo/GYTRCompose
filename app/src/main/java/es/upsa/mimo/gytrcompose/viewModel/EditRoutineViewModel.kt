package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine

class EditRoutineViewModel(application: Application): AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    suspend fun getRoutineById(routineId: Int): Routine? = repository.getRoutineById(routineId)

    fun getRoutineExercises(routineId: Int): LiveData<List<String>?> = repository.getRoutineExercises(routineId)

    suspend fun getExercisesById(exerciseId: List<String>): List<Exercise>? = repository.getExercisesByIds(exerciseId)

    suspend fun updateRoutine(routine: Routine) = repository.updateRoutine(routine)

    suspend fun addExerciseToRoutine(routineId: Int, exerciseId: String) {
        repository.addExerciseToRoutine(routineId = routineId, exerciseId = exerciseId)
    }

    suspend fun deleteExerciseFromRoutine(routineId: Int, exerciseId: String) = repository.deleteExerciseFromRoutine(routineId, exerciseId)

    suspend fun getRoutineByName(name: String): Routine? = repository.getRoutineByName(name)
}