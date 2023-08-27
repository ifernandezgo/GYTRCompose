package es.upsa.mimo.gytrcompose.db

import android.app.Application
import androidx.lifecycle.LiveData
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.History
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.RoutineExerciseCrossRef
import es.upsa.mimo.gytrcompose.model.Serie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbRepository(application: Application) {

    private val routineDao: RoutineDao
    private val exerciseDao: ExerciseDao
    private val historyDao: HistoryDao
    private val serieDao: SerieDao
    private val routineExerciseDao: RoutineExerciseDao

    init {
        val db: GYTRDatabase = GYTRDatabase.getInstance(application.applicationContext)!!
        routineDao = db.routineDao()
        exerciseDao = db.exerciseDao()
        historyDao = db.historyDao()
        serieDao = db.serieDao()
        routineExerciseDao = db.routineExerciseDao()
    }

    fun getAllRoutines(): LiveData<List<Routine>> {
        return routineDao.getAllRoutines()
    }

    suspend fun getRoutineById(routineId: Int): Routine? = withContext(Dispatchers.IO) {
        routineDao.getRoutineById(routineId)
    }

    suspend fun getRoutineByName(name: String): Routine? = withContext(Dispatchers.IO) {
        routineDao.getRoutineByName(name)
    }

    suspend fun getRoutinesCount(): Int = withContext(Dispatchers.IO) {
        routineDao.getRoutinesCount()
    }

    suspend fun insertRoutine(routine: Routine): Long = withContext(Dispatchers.IO){
        routineDao.insertRoutine(routine)
    }

    suspend fun updateRoutine(routine: Routine) = withContext(Dispatchers.IO) {
        routineDao.updateRoutine(routine)
    }

    suspend fun addExerciseToRoutine(routineId: Int, exerciseId: String) = withContext(Dispatchers.IO) {
        val crossRef = RoutineExerciseCrossRef(routineId = routineId, exerciseId = exerciseId)
        routineExerciseDao.insertRoutineWithExercise(crossRef)
    }

    fun getRoutineExercises(routineId: Int): LiveData<List<String>?> {
        return routineExerciseDao.getRoutineExercises(routineId)
    }

    fun getExercisesByRoutineId(routineId: Int): LiveData<List<RoutineExerciseCrossRef>?> {
        return routineExerciseDao.getExercisesByRoutineId(routineId)
    }

    suspend fun deleteRoutine(routine: Routine, routineExercises: List<RoutineExerciseCrossRef>?) = withContext(Dispatchers.IO) {
        historyDao.deleteHistoryByRoutine(routine.routineId!!)
        routineDao.deleteRoutine(routine = routine, routineExercises = routineExercises)
    }

    suspend fun getExerciseById(exerciseId: String): Exercise? = withContext(Dispatchers.IO) {
        exerciseDao.getExerciseById(exerciseId)
    }

    suspend fun getExercisesByIds(exercisesId: List<String>): List<Exercise>? = withContext(Dispatchers.IO) {
        exerciseDao.getExercisesByIds(exercisesId)
    }

    fun getAllExercises(): LiveData<List<Exercise>> {
        return exerciseDao.getAllExercises()
    }

    suspend fun insertExercise(exercise: Exercise) = withContext(Dispatchers.IO) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun deleteExerciseFromRoutine(routineId: Int, exerciseId: String) = withContext(Dispatchers.IO) {
        routineExerciseDao.deleteExerciseFromRoutine(RoutineExerciseCrossRef(exerciseId, routineId))
    }

    suspend fun getExerciseLastSeries(exerciseId: String, limit: Int): List<Serie> = withContext(Dispatchers.IO) {
        serieDao.getExerciseLastSeries(exerciseId, limit)
    }

    suspend fun insertSeries(series: List<Serie>) = withContext(Dispatchers.IO) {
        for(serie in series) {
            serieDao.insertSerie(serie)
        }
    }

    suspend fun insertHistory(history: History): Long = withContext(Dispatchers.IO) {
        historyDao.insertHistory(history)
    }

    fun getMyHistories(): LiveData<List<History>> {
        return historyDao.getHistories()
    }

    suspend fun getSerieByHistoryId(historyId: Int): List<Serie> = withContext(Dispatchers.IO) {
        serieDao.getSerieByHistoryId(historyId)
    }
}