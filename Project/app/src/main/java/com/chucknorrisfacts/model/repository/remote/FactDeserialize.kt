package com.chucknorrisfacts.model.repository.remote

import com.domain.Category
import com.domain.Fact
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class FactDeserialize : JsonDeserializer<Fact> {
    private val uncategorized = "uncategorized"

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Fact {
        val jsonObject = json?.asJsonObject
        val category = Category()
        val fact = Fact()
        fact.value = jsonObject?.get("value")?.asString
        fact.url = jsonObject?.get("url")?.asString
        fact.hash = jsonObject?.get("id")?.asString
        fact.icon = jsonObject?.get("icon_url")?.asString
        category.name = jsonObject?.get("categories")?.let {
            if (it.isJsonNull) {
                uncategorized
            } else {
                Gson().fromJson(it.asJsonArray, List::class.java)?.let {categories ->
                    if(categories.isNotEmpty()) {
                        categories[0] as String
                    } else {
                        uncategorized
                    }
                }?: run {
                    uncategorized
                }
            }
        }?: run {
            uncategorized
        }

        fact.category = category

        return fact
    }
}