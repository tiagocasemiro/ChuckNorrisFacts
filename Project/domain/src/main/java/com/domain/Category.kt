package com.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Ignore
import com.google.gson.annotations.Expose

@Entity(indices = [Index(value = ["name"],  unique = true)])
data class Category(@ColumnInfo(name = "name") var name: String? = null) {
    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    constructor() : this(name = null)
}