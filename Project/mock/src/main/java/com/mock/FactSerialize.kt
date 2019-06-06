package com.mock

import com.domain.Fact
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonArray


class FactSerialize : JsonSerializer<Fact> {

    override fun serialize(src: Fact?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()

        jsonObject.addProperty("value", src?.value)
        jsonObject.addProperty("url", src?.url)
        jsonObject.addProperty("id", src?.hash)
        jsonObject.addProperty("icon_url", src?.icon)
        src?.category?.name?.let {
            val jsonAuthorsArray = JsonArray()
            jsonAuthorsArray.add(JsonPrimitive(src?.category?.name))
            jsonObject.add("categories", jsonAuthorsArray)
        }

        return jsonObject
    }
}