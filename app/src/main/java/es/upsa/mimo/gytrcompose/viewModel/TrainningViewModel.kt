package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.History
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.Serie

class TrainingViewModel(application: Application): AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    suspend fun getRoutineById(routineId: Int): Routine? = repository.getRoutineById(routineId)

    fun getRoutineExercises(routineId: Int): LiveData<List<String>?> = repository.getRoutineExercises(routineId)

    suspend fun getExercisesById(exerciseId: List<String>): List<Exercise>? = repository.getExercisesByIds(exerciseId)

    suspend fun getExerciseLastSeries(exerciseId: String, limit: Int): List<Serie> = repository.getExerciseLastSeries(exerciseId, limit)

    suspend fun insertSeries(series: List<Serie>) = repository.insertSeries(series)

    suspend fun insertHistory(history: History): Long = repository.insertHistory(history)
}