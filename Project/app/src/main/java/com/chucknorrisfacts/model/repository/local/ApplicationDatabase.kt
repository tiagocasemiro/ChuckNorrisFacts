package com.chucknorrisfacts.model.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [CategoryDao::class, SearchedDao::class], version = 1, exportSchema = true)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun searchedDao(): SearchedDao
    abstract fun categoryDao(): CategoryDao
}