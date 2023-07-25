package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import es.upsa.mimo.gytrcompose.db.DbRepository

class MyExercisesViewModel(application: Application): AndroidViewModel(application) {

    private var repository: DbRepository = DbRepository(application)

    fun getMyExercises() = repository.getAllExercises()
}