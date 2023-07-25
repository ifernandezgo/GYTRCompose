package es.upsa.mimo.gytrcompose.network

import es.upsa.mimo.gytrcompose.common.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface APIService {

    @Headers(
        "X-RapidAPI-Key: " + Constants.apiKey,
        "X-RapidAPI-Host: " + Constants.apiHost
    )
    @GET("exercises/name/{name}")
    suspend fun getExercisesByName(
        @Path("name") name: String
    ): Response<List<ExerciseDecoder>>

    @Headers(
        "X-RapidAPI-Key: " + Constants.apiKey,
        "X-RapidAPI-Host: " + Constants.apiHost
    )
    @GET("/exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String
    ): Response<List<ExerciseDecoder>>

    @Headers(
        "X-RapidAPI-Key: " + Constants.apiKey,
        "X-RapidAPI-Host: " + Constants.apiHost
    )
    @GET("exercises/target/{muscle}")
    suspend fun getExercisesByMuscle(
        @Path("muscle") muscle: String
    ): Response<List<ExerciseDecoder>>

    @Headers(
        "X-RapidAPI-Key: " + Constants.apiKey,
        "X-RapidAPI-Host: " + Constants.apiHost
    )
    @GET("exercises/exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") id: String
    ): Response<ExerciseDecoder>

}