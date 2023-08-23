package es.upsa.mimo.gytrcompose.network

class APIRepository(private val service: APIService) {

    suspend fun getExerciseByName(name: String): List<ExerciseDecoder> {
        val query = name.lowercase()
        val response = service.getExercisesByName(query)
        return if(response.isSuccessful) {
            response.body()!!
        } else {
            listOf()
        }
    }

    suspend fun getExercisesByBodyPart(bodyPart: String): List<ExerciseDecoder> {
        val query = bodyPart.lowercase()
        val response = service.getExercisesByBodyPart(query)
        return if(response.isSuccessful) {
            response.body()!!
        } else {
            listOf()
        }
    }

    suspend fun getExercisesByMuscle(muscle: String): List<ExerciseDecoder> {
        val query = muscle.lowercase()
        val response = service.getExercisesByMuscle(query)
        return if(response.isSuccessful) {
            response.body()!!
        } else {
            listOf()
        }
    }

    suspend fun getExercisesById(id: String): ExerciseDecoder? {
        val response = service.getExerciseById(id)
        return if(response.isSuccessful) {
            response.body()!!
        } else {
            null
        }
    }
}