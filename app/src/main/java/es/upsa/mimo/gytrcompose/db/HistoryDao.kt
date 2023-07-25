package es.upsa.mimo.gytrcompose.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import es.upsa.mimo.gytrcompose.model.History

@Dao
interface HistoryDao {

    @Insert
    fun insertHistory(history: History): Long

    @Query("SELECT * FROM history ORDER BY date DESC LIMIT 20")
    fun getHistories(): LiveData<List<History>>

    @Query("DELETE FROM history WHERE routineId = :routineId")
    fun deleteHistoryByRoutine(routineId: Int)
}