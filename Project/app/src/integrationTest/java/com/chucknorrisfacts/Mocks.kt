package com.chucknorrisfacts

import com.domain.Category
import com.domain.Fact
import com.domain.Search
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mock.FactSerialize

fun mockJsonSearchFromApi() : String {
    val facts: MutableList<Fact> = mutableListOf()

    var fact = Fact()
    fact.category = Category("movie")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "tng5xzi5t9syvqaubukycw"
    fact.url = "/jokes/tng5xzi5t9syvqaubukycw"
    fact.value = "Chuck Norris always knows the EXACT location of Carmen SanDiego."
    facts.add(fact)

    fact = Fact()
    fact.category = Category("science")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "DuhjnnJCQmKAeMECnYTJuA"
    fact.url = "/jokes/DuhjnnJCQmKAeMECnYTJuA"
    fact.value = "Jack in the Box's do not work around Chuck Norris. They know better than to attempt to scare Chuck Norris"
    facts.add(fact)

    fact = Fact()
    fact.category = null
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "pjepanwfqgowpgc3uf_7hg"
    fact.url = "/jokes/pjepanwfqgowpgc3uf_7hg"
    fact.value = "Chuck Norris doesn't look both ways before he crosses the street... he just roundhouses any cars that get too close."
    facts.add(fact)

    fact = Fact()
    fact.category = null
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "zBbfIKTrQx2ouSCLvXkW4w"
    fact.url = "/jokes/zBbfIKTrQx2ouSCLvXkW4w"
    fact.value = "Chuck norris is only scared of one thing his reflection fact"
    facts.add(fact)

    val search = Search()
    search.total = 4
    search.result = facts.toList()

    return  GsonBuilder().registerTypeAdapter(Fact::class.java, FactSerialize()).create().toJson(search)
}

fun mockJsonCategoryFromApi() : String {
    val categoriesFromApi: MutableList<String> = mutableListOf()

    categoriesFromApi.add("fashion")
    categoriesFromApi.add("money")
    categoriesFromApi.add("career")
    categoriesFromApi.add("travel")
    categoriesFromApi.add("music")
    categoriesFromApi.add("history")
    categoriesFromApi.add("animal")
    categoriesFromApi.add("religion")
    categoriesFromApi.add("political")
    categoriesFromApi.add("sport")
    categoriesFromApi.add("science")
    categoriesFromApi.add("celebrity")
    categoriesFromApi.add("food")
    categoriesFromApi.add("movie")
    categoriesFromApi.add("dev")
    categoriesFromApi.add("explicit")

    return Gson().toJson(categoriesFromApi.toList())
}


fun mockCategoriesObjectFromApi() : List<String> {
    val categoriesFromApi: MutableList<String> = mutableListOf()

    categoriesFromApi.add("fashion")
    categoriesFromApi.add("money")
    categoriesFromApi.add("career")
    categoriesFromApi.add("travel")
    categoriesFromApi.add("music")
    categoriesFromApi.add("history")
    categoriesFromApi.add("animal")
    categoriesFromApi.add("religion")
    categoriesFromApi.add("political")
    categoriesFromApi.add("sport")
    categoriesFromApi.add("science")
    categoriesFromApi.add("celebrity")
    categoriesFromApi.add("food")
    categoriesFromApi.add("movie")
    categoriesFromApi.add("dev")
    categoriesFromApi.add("explicit")

    return categoriesFromApi.toList()
}

fun mockSearchFromIntent() : Search {
    val facts: MutableList<Fact> = mutableListOf()

    var fact = Fact()
    fact.category = Category("movie")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "tng5xzi5t9syvqaubukycw"
    fact.url = "/jokes/tng5xzi5t9syvqaubukycw"
    fact.value = "Chuck Norris always knows the EXACT location of Carmen SanDiego."
    facts.add(fact)

    fact = Fact()
    fact.category = Category("science")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "DuhjnnJCQmKAeMECnYTJuA"
    fact.url = "/jokes/DuhjnnJCQmKAeMECnYTJuA"
    fact.value = "Jack in the Box's do not work around Chuck Norris. They know better than to attempt to scare Chuck Norris"
    facts.add(fact)

    fact = Fact()
    fact.category = Category("uncategorized")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "pjepanwfqgowpgc3uf_7hg"
    fact.url = "/jokes/pjepanwfqgowpgc3uf_7hg"
    fact.value = "Chuck Norris doesn't look both ways before he crosses the street... he just roundhouses any cars that get too close."
    facts.add(fact)

    fact = Fact()
    fact.category = Category("uncategorized")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "zBbfIKTrQx2ouSCLvXkW4w"
    fact.url = "/jokes/zBbfIKTrQx2ouSCLvXkW4w"
    fact.value = "Chuck norris is only scared of one thing his reflection fact"
    facts.add(fact)

    val search = Search()
    search.total = 4
    search.result = facts.toList()

    return  search
}