package com.domain

import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

class Category {
    @Expose(serialize = false, deserialize = false)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = null
}