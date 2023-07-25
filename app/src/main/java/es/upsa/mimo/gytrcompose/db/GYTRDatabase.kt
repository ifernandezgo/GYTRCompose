package es.upsa.mimo.gytrcompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.upsa.mimo.gytrcompose.common.Constants
import es.upsa.mimo.gytrcompose.model.Exercise
import es.upsa.mimo.gytrcompose.model.History
import es.upsa.mimo.gytrcompose.model.Routine
import es.upsa.mimo.gytrcompose.model.Serie
import es.upsa.mimo.gytrcompose.model.RoutineExerciseCrossRef

@Database(entities = [Exercise::class, History::class, Routine::class, Serie::class, RoutineExerciseCrossRef::class], version = 1)
abstract class GYTRDatabase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun historyDao(): HistoryDao
    abstract fun routineDao(): RoutineDao
    abstract fun serieDao(): SerieDao
    abstract fun routineExerciseDao(): RoutineExerciseDao

    companion object {

        private var instance: GYTRDatabase? = null
        fun getInstance(context: Context): GYTRDatabase? {
            if (instance == null) {
                synchronized(GYTRDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        GYTRDatabase::class.java,
                        Constants.dbName
                    ).build()
                }
            }

            return instance
        }
    }
}