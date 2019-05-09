package com.chucknorrisfacts.model.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.domain.Category

@Dao
interface CategoryDao {
    @Insert
    fun add(vararg category: Category)

    @Delete
    fun delete(vararg category: Category)

    @Query("SELECT * FROM category")
    fun all(): List<Category>
}