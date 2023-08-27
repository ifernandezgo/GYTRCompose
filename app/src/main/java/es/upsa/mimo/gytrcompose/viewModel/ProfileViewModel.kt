package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.History
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.Serie

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    fun getMyHistories(): LiveData<List<History>> = repository.getMyHistories()

    suspend fun getRoutineById(routineId: Int): Routine? = repository.getRoutineById(routineId)

    suspend fun getSerieByHistoryId(historyId: Int): List<Serie> = repository.getSerieByHistoryId(historyId)

    suspend fun getExerciseById(exerciseId: String): Exercise? = repository.getExerciseById(exerciseId)
}