package com.chucknorrisfacts.model.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.domain.Searched

@Dao
interface SearchedDao {
    @Insert(onConflict = REPLACE)
    fun add(vararg searched: Searched)

    @Query("SELECT * FROM searched")
    fun all(): List<Searched>
}