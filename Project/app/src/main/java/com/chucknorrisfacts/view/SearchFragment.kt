package com.chucknorrisfacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.chucknorrisfacts.R
import com.chucknorrisfacts.controller.SearchController
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.ext.android.inject


class SearchFragment : Fragment() {

    private val searchController: SearchController by inject()
    private val load: Load by lazy {
        Load(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.chucknorrisfacts.R.layout.fragment_search, container, false)


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

        var chip = Chip(context, null, R.attr.chipStyle)
        chip.text = "Name Surname"
        view.chipGroup.addView(chip as View)

        //load.show()

        val list = mutableListOf<String>()
        list.add("item")
        list.add("item")
        list.add("item")
        list.add("item")
        list.add("item")
        list.add("item")

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        view.searched.adapter = adapter

        return view
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
