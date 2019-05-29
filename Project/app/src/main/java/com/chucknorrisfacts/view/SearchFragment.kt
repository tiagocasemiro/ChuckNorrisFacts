package com.chucknorrisfacts.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.isConnected
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
        Load(activity!! as Activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.chucknorrisfacts.R.layout.fragment_search, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(com.chucknorrisfacts.R.string.search_title)

        isConnected(noConnection)
        searchController.categories(loadCategories, failToLoadData)
        searchController.searcheds(loadSearcheds, noResult)

        view?.query?.setOnEditorActionListener { query, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                searchController.searchWith(query.text.toString(), loadFacts, failToLoadData, loadSearcheds, noResult)
                load.show()
            }

            return@setOnEditorActionListener false
        }

        load.show()

        return view
    }

    private val loadSearcheds : (searcheds: List<Searched>) -> Unit = { searcheds ->
        val list = searcheds.distinctBy { Pair(it.query, it.query) }.map { it.query }.reversed().toList()

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, list)
        view?.searched?.adapter = adapter
        view?.searched?.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            searchController.searchWith(list[position]!!, loadFacts, failToLoadData)
            load.show()
        }
        adapter.notifyDataSetChanged()
        if (list.isNotEmpty())
            searchedLabel.visibility = View.VISIBLE
        else
            searchedLabel.visibility = View.INVISIBLE
    }

    private val loadFacts : (search: Search) -> Unit = { search ->
        //TODO implement
        load.hide()
        Toast.makeText(context, search.total.toString(), Toast.LENGTH_SHORT).show()
    }

    private val loadCategories : (categories: List<Category>) -> Unit = { categories ->
        categories.forEach { category ->
            val chip = Chip(context, null, R.attr.chipStyle)
            chip.text = category.name
            chip.setOnClickListener { chip ->
                searchController.searchWith((chip as Chip).text.toString(), loadFacts, failToLoadData, loadSearcheds, noResult)
                load.show()
            }
            view?.chipGroup?.addView(chip as View)
        }
        if(categories.isNotEmpty())
            categoryLabel.visibility = View.VISIBLE
         else
            categoryLabel.visibility = View.INVISIBLE

        load.hide()
    }

    private val failToLoadData : () -> Unit = {

    }

    private val noConnection : () -> Unit = {

    }

    private val noResult : () -> Unit = {

    }


}
