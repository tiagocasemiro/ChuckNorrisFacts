package com.domain

import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

class PastSearch {
    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var term: String? = null
}