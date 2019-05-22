package com.chucknorrisfacts.model.repository.remote

import com.domain.Category
import com.domain.Fact
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class FactDeserialize : JsonDeserializer<Fact> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Fact {
        val jsonObject = json?.asJsonObject
        val category = Category()
        val fact = Fact()
        fact.value = jsonObject?.get("value")?.asString
        fact.url = jsonObject?.get("url")?.asString
        fact.hash = jsonObject?.get("id")?.asString
        fact.icon = jsonObject?.get("icon_url")?.asString
        category.name = jsonObject?.get("category")?.let {
            if (it.isJsonNull) "uncategorized" else it.asString
        }?: run {
            "uncategorized"
        }
        fact.category = category

        return fact
    }
}