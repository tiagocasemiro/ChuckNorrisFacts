package com.chucknorrisfacts.model.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.domain.Searched

@Dao
interface SearchedDao {
    @Insert
    fun add(vararg searched: Searched)

    @Query("SELECT * FROM searched")
    fun all(): List<Searched>
}