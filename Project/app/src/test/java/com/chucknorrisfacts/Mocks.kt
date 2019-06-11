package com.chucknorrisfacts

import com.domain.Category
import com.domain.Fact
import com.domain.Search
import com.domain.Searched

fun mockSearchFromApi() : Search {
    val facts: MutableList<Fact> = mutableListOf()

    var fact = Fact()
    fact.category = Category("movie")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "tng5xzi5t9syvqaubukycw"
    fact.url = "https://api.chucknorris.io/jokes/tng5xzi5t9syvqaubukycw"
    fact.value = "Chuck Norris always knows the EXACT location of Carmen SanDiego."
    facts.add(fact)

    fact = Fact()
    fact.category = Category("science")
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "DuhjnnJCQmKAeMECnYTJuA"
    fact.url = "https://api.chucknorris.io/jokes/DuhjnnJCQmKAeMECnYTJuA"
    fact.value = "Jack in the Box's do not work around Chuck Norris. They know better than to attempt to scare Chuck Norris"
    facts.add(fact)

    fact = Fact()
    fact.category = null
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "pjepanwfqgowpgc3uf_7hg"
    fact.url = "https://api.chucknorris.io/jokes/pjepanwfqgowpgc3uf_7hg"
    fact.value = "Chuck Norris doesn't look both ways before he crosses the street... he just roundhouses any cars that get too close."
    facts.add(fact)

    fact = Fact()
    fact.category = null
    fact.icon = "https://assets.chucknorris.host/img/avatar/chuck-norris.png"
    fact.hash = "zBbfIKTrQx2ouSCLvXkW4w"
    fact.url = "https://api.chucknorris.io/jokes/zBbfIKTrQx2ouSCLvXkW4w"
    fact.value = "Chuck norris is only scared of one thing his reflection fact"
    facts.add(fact)

    val search = Search()
    search.total = 4
    search.result = facts.toList()

    return search
}

fun mockCategoriesFromApi() : List<String> {
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

fun mockCategoriesFromDb() : List<Category> {
    val categoriesFromDb: MutableList<Category> = mutableListOf()

    categoriesFromDb.add(Category("fashion"))
    categoriesFromDb.add(Category("money"))
    categoriesFromDb.add(Category("career"))
    categoriesFromDb.add(Category("travel"))
    categoriesFromDb.add(Category("music"))
    categoriesFromDb.add(Category("history"))
    categoriesFromDb.add(Category("animal"))
    categoriesFromDb.add(Category("religion"))
    categoriesFromDb.add(Category("political"))
    categoriesFromDb.add(Category("sport"))
    categoriesFromDb.add(Category("science"))
    categoriesFromDb.add(Category("celebrity"))
    categoriesFromDb.add(Category("food"))
    categoriesFromDb.add(Category("movie"))
    categoriesFromDb.add(Category("dev"))
    categoriesFromDb.add(Category("explicit"))

    return categoriesFromDb.toList()
}

fun mockSearchedFromDb() : List<Searched> {
    val searchdsFromDb = mutableListOf<Searched>()

    searchdsFromDb.add(Searched("car"))
    searchdsFromDb.add(Searched("movie"))
    searchdsFromDb.add(Searched("history"))
    searchdsFromDb.add(Searched("hause"))
    searchdsFromDb.add(Searched("kick"))
    searchdsFromDb.add(Searched("lend"))

    return searchdsFromDb.toList()
}