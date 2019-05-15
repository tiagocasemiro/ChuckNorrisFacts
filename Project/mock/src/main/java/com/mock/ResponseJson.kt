package com.mock

import com.domain.Fact
import com.google.gson.Gson

class ResponseJson {
    fun search() : String {
        val result: MutableList<Fact> = mutableListOf()

        var fact = Fact()
        fact.category = null
        fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
        fact.hash = "tng5xzi5t9syvqaubukycw"
        fact.url = "https://api.chucknorris.io/jokes/tng5xzi5t9syvqaubukycw"
        fact.value = "Chuck Norris always knows the EXACT location of Carmen SanDiego."
        result.add(fact)

        fact = Fact()
        fact.category = null
        fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
        fact.hash = "DuhjnnJCQmKAeMECnYTJuA"
        fact.url = "https://api.chucknorris.io/jokes/DuhjnnJCQmKAeMECnYTJuA"
        fact.value = "Jack in the Box's do not work around Chuck Norris. They know better than to attempt to scare Chuck Norris"
        result.add(fact)

        fact = Fact()
        fact.category = null
        fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
        fact.hash = "pjepanwfqgowpgc3uf_7hg"
        fact.url = "https://api.chucknorris.io/jokes/pjepanwfqgowpgc3uf_7hg"
        fact.value = "Chuck Norris doesn't look both ways before he crosses the street... he just roundhouses any cars that get too close."
        result.add(fact)

        fact = Fact()
        fact.category = null
        fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
        fact.hash = "zBbfIKTrQx2ouSCLvXkW4w"
        fact.url = "https://api.chucknorris.io/jokes/zBbfIKTrQx2ouSCLvXkW4w"
        fact.value = "Chuck norris is only scared of one thing his reflection fact"
        result.add(fact)

        return Gson().toJson(result)
    }

    fun categories() : String {
        val result: MutableList<String> = mutableListOf()

        result.add("fashion")
        result.add("money")
        result.add("career")
        result.add("travel")
        result.add("music")
        result.add("history")
        result.add("animal")
        result.add("religion")
        result.add("political")
        result.add("sport")
        result.add("science")
        result.add("celebrity")
        result.add("food")
        result.add("movie")
        result.add("dev")
        result.add("explicit")

        return Gson().toJson(result)
    }
}