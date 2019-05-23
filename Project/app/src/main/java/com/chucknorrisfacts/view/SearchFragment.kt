package com.chucknorrisfacts.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chucknorrisfacts.R
import com.chucknorrisfacts.controller.SearchController
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    val searchController: SearchController by inject()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Chips  layout
        //https://material.io/develop/android/components/chip/

        //Flow layout
        //https://github.com/nex3z/FlowLayout

        //Flex box layout
        //https://github.com/google/flexbox-layout


        (activity as AppCompatActivity).supportActionBar?.title = ""

        searchController.categories({
            loadCategories(it)
        }, {
            fail(it)
        })

       searchController.searchWith("car", {
            loadFacts(it)
        }, {
            fail(it)
        })

        searchController.searcheds({
            loadSearcheds(it)
        }, {
            fail(it)
        })


        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private fun loadSearcheds(searcheds: List<Searched>) {
        System.out.println(searcheds.size)
    }

    private fun loadFacts(search: Search) {
        System.out.println(search.total)
    }

    private fun loadCategories(categories: List<Category>) {
        System.out.println(categories.size)
    }

    private fun fail(exception: Exception) {
        exception.printStackTrace()
    }
}
