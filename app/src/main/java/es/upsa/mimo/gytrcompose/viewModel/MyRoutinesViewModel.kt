package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Routine

class MyRoutinesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    fun getRoutines(): LiveData<List<Routine>> = repository.getAllRoutines()

    suspend fun getRoutinesCount(): Int = repository.getRoutinesCount()
}