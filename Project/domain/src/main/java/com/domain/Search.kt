package com.domain

import java.io.Serializable

class Search : Serializable{
    var total: Long? = null
    var result: List<Fact>? = null

    fun emptySeach() : Search {
        total = 0
        result = emptyList()

        return this
    }
}