package com.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(indices = [Index(value = ["query"],  unique = true)])
class Searched(query: String? = null) {
    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "query")
    var query: String? = null

    init {
        this.query = query
    }
 }