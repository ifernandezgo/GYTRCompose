package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.Routine

class NewRoutineViewModel(application: Application): AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    suspend fun getExerciseById(exerciseId: String): Exercise? = repository.getExerciseById(exerciseId)

    suspend fun insertRoutine(routine: Routine, exercises: List<Exercise>?) {
        val routineId = repository.insertRoutine(routine)
        if (exercises != null) {
            for(exercise in exercises) {
                repository.addExerciseToRoutine(routineId = routineId.toInt(), exerciseId = exercise.exerciseId)
            }
        }
    }

    suspend fun getRoutineByName(name: String): Routine? = repository.getRoutineByName(name)
}