package es.upsa.mimo.gytrcompose.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.upsa.mimo.gytrcompose.common.Constants
import es.upsa.mimo.gytrcompose.network.APIRepository
import es.upsa.mimo.gytrcompose.network.APIService
import es.upsa.mimo.gytrcompose.network.ExerciseDecoder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExercisesViewModel : ViewModel() {

    private val exercises = MutableLiveData<List<ExerciseDecoder>>()
    fun getExercises(): LiveData<List<ExerciseDecoder>> = exercises

    private val repository: APIRepository by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(APIService::class.java)
        APIRepository(service)
    }

    suspend fun getExerciseByName(name: String) {
        viewModelScope.launch {
            exercises.value = repository.getExerciseByName(name)
        }
    }

    suspend fun getExerciseByBodyPart(bodyPart: String) {
        viewModelScope.launch {
            exercises.value = repository.getExercisesByBodyPart(bodyPart)
        }
    }

    suspend fun getExerciseByMuscle(muscle: String) {
        viewModelScope.launch {
            exercises.value = repository.getExercisesByMuscle(muscle)
        }
    }
}