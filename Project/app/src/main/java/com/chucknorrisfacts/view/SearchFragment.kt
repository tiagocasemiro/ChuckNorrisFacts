package com.chucknorrisfacts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.isConnected
import com.chucknorrisfacts.controller.SearchController
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private val searchController: SearchController by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Chips  layout
        //https://material.io/develop/android/components/chip/

        //Flow layout
        //https://github.com/nex3z/FlowLayout

        //Flex box layout
        //https://github.com/google/flexbox-layout


       // (activity as AppCompatActivity).supportActionBar?.title = ""

      //  isConnected(noConnection)
       // searchController.categories(loadCategories, failToLoadData)
       // searchController.searchWith("car", loadFacts, failToLoadData)
        //searchController.searcheds(loadSearcheds, noResult)

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private val loadSearcheds : (searcheds: List<Searched>) -> Unit = { searcheds ->

    }

    private val loadFacts : (search: Search) -> Unit = { search ->

    }

    private val loadCategories : (categories: List<Category>) -> Unit = { categories ->

    }

    private val failToLoadData : () -> Unit = {

    }

    private val noConnection : () -> Unit = {

    }

    private val noResult : () -> Unit = {

    }
}
