package es.upsa.mimo.gytrcompose.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.upsa.mimo.gytrcompose.model.Serie

@Dao
interface SerieDao {

    @Insert
    fun insertSerie(serie: Serie)

    @Query("SELECT * FROM serie WHERE exerciseId = :exerciseId ORDER BY date DESC LIMIT :limit")
    fun getExerciseLastSeries(exerciseId: String, limit: Int): List<Serie>

    @Query("SELECT * FROM serie WHERE historyId = :historyId")
    fun getSerieByHistoryId(historyId: Int): List<Serie>
}