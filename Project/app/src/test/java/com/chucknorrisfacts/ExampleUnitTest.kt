package com.chucknorrisfacts

import com.chucknorrisfacts.model.repository.remote.FactDeserialize
import com.domain.Fact
import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `Deve - retornar o nome uncategorized Quando - category nao ter atributo category no json fact`() {
        val json = "{\n" +
                "\"name\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        val gsonBuilder = GsonBuilder()
        val deserializer = FactDeserialize()
        gsonBuilder.registerTypeAdapter(Fact::class.java, deserializer)
        val gson = gsonBuilder.create()
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        Assert.assertEquals(fact.category?.name, "uncategorized")
    }

    @Test
    fun `Deve - retornar o nome uncategorized Quando - category for null no json fact`() {
        val json = "{\n" +
                "\"category\": null,\n" +
                "\"name\": \"https://assets.chucknorris.host/img/avatar/chuck-norris.png\",\n" +
                "\"id\": \"6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"url\": \"https://api.chucknorris.io/jokes/6roGqN7rRL2IC1JxQJE1Ag\",\n" +
                "\"value\": \"Chuck Norris killed The Fat Lady because it's not over until Chuck Norris sings.\"\n" +
                "}"

        val gsonBuilder = GsonBuilder()
        val deserializer = FactDeserialize()
        gsonBuilder.registerTypeAdapter(Fact::class.java, deserializer)
        val gson = gsonBuilder.create()
        val fact = gson.fromJson<Fact>(json, Fact::class.java)

        Assert.assertEquals(fact.category?.name, "uncategorized")
    }
}
