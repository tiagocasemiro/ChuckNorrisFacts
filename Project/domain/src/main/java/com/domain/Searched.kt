package com.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
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

    override fun equals(other: Any?): Boolean {
        if(other is Searched)
            return (other.query != null && this.query != null && other.query.equals(this.query))

        return false
    }
}