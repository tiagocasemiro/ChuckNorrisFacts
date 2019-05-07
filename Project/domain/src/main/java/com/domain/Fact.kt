package com.domain

import com.google.gson.annotations.SerializedName

class Fact {
    var category: Category? = null
    @SerializedName("icon_url")
    var icon: String? = null
    @SerializedName("id")
    var hash: String? = null
    var url: String? = null
    var value: String? = null
}

