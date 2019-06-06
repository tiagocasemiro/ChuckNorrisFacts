package com.chucknorrisfacts.model.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import com.domain.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = ABORT)
    fun add(vararg category: Category)

    @Query("SELECT * FROM category")
    fun all() : List<Category>
}