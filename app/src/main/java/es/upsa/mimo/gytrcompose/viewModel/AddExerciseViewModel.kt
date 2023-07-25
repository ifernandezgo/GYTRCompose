package es.upsa.mimo.gytrcompose.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import es.upsa.mimo.gytrcompose.common.Constants
import es.upsa.mimo.gytrcompose.db.DbRepository
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.network.APIRepository
import es.upsa.mimo.gytrcompose.network.APIService
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddExerciseViewModel(application: Application): AndroidViewModel(application) {

    private var dbRepository: DbRepository = DbRepository(application)

    private val exercises = MutableLiveData<List<ExerciseDecoder>>()
    fun getExercises(): LiveData<List<ExerciseDecoder>> = exercises

    private val ApiRepository: APIRepository by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(APIService::class.java)
        APIRepository(service)
    }

    suspend fun getExerciseByName(name: String) {
        viewModelScope.launch {
            exercises.value = ApiRepository.getExerciseByName(name)
        }
    }

    suspend fun getExerciseByBodyPart(bodyPart: String) {
        viewModelScope.launch {
            exercises.value = ApiRepository.getExercisesByBodyPart(bodyPart)
        }
    }

    suspend fun getExerciseByMuscle(muscle: String) {
        viewModelScope.launch {
            exercises.value = ApiRepository.getExercisesByMuscle(muscle)
        }
    }

    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return dbRepository.getExerciseById(exerciseId)
    }

    suspend fun insertExercise(exercise: Exercise) {
        dbRepository.insertExercise(exercise)
    }
}