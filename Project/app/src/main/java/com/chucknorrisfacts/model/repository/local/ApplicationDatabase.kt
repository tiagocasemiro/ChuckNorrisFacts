package com.chucknorrisfacts.model.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.domain.Category
import com.domain.Searched

@Database(entities = [Category::class, Searched::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun searchedDao(): SearchedDao
    abstract fun categoryDao(): CategoryDao
}