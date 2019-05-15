package com.chucknorrisfacts.model.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.domain.Category
import com.domain.Searched

@Database(entities = [Category::class, Searched::class], version = 1, exportSchema = true)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun searchedDao(): SearchedDao
    abstract fun categoryDao(): CategoryDao
}