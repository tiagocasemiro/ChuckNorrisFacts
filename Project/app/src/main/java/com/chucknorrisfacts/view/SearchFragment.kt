package com.chucknorrisfacts.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.hideKeyboard
import com.chucknorrisfacts.configuration.isConnected
import com.chucknorrisfacts.controller.SearchController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.domain.Category
import com.domain.Search
import com.domain.Searched
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class SearchFragment : Fragment() {

    private val searchController: SearchController by inject()
    private val load: Load by lazy {
        Load(activity!! as Activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchController.categories(loadCategories, failToLoadData)
        searchController.searcheds(loadSearcheds, noResult)

        view?.query?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                view.queryInputLayout.error = null
            }
        })

        view?.query?.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                view.queryInputLayout.error = null
                if(isConnected(noConnection)) {
                    if(view.query.text?.trim().toString().isNotEmpty()) {
                        searchController.searchWith(
                            view.query.text.toString(),loadFacts, failToLoadData, loadSearcheds
                        )
                        load.show()
                        return@setOnEditorActionListener false
                    } else {
                        YoYo.with(Techniques.Shake).duration(350).playOn(view.queryInputLayout)
                        view.queryInputLayout.error = context!!.getString(R.string.message_required_field)
                    }
                }
            }

            return@setOnEditorActionListener true
        }

        view.setOnTouchListener { v, _ ->
            if (v !is EditText) {
                activity!!.hideKeyboard()
            }
            false
        }


        load.show()

        return view
    }

    override fun onPause() {
        super.onPause()
        load.hide()
    }

    private val loadSearcheds : (searcheds: List<Searched>) -> Unit = { searcheds ->
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val list = searcheds.distinctBy { Pair(it.query, it.query) }.map { it.query }.toList()

                val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, list)
                view?.searched?.adapter = adapter
                view?.searched?.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
                    if (isConnected(noConnection)) {
                        view?.query?.setText(list[position]!!)
                        searchController.searchWith(list[position]!!, loadFacts, failToLoadData)
                        activity?.hideKeyboard()
                        load.show()
                    }
                }
                adapter.notifyDataSetChanged()
                if (list.isNotEmpty())
                    searchedLabel.visibility = View.VISIBLE
                else
                    searchedLabel.visibility = View.INVISIBLE
            }
        }
    }

    private val loadFacts : (search: Search) -> Unit = { search ->
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val arguments = Bundle().apply {
                    putSerializable(Search::class.java.canonicalName, search)
                }
                (activity!! as MainActivity).intent?.putExtras(arguments)
                load.hide()
                view?.findNavController()?.popBackStack()
            }
        }
    }

    private val loadCategories : (categories: List<Category>) -> Unit = { categories ->
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                categories.forEach { category ->
                    val chip = Chip(context, null, R.attr.chipStyle)
                    chip.text = category.name
                    chip.setOnClickListener {
                        if (isConnected(noConnection)) {
                            view?.query?.setText(chip.text.toString())
                            searchController.searchWith(chip.text.toString(), loadFacts, failToLoadData, loadSearcheds)
                            activity?.hideKeyboard()
                            load.show()
                        }
                    }
                    view?.chipGroup?.addView(chip as View)
                }
                if (categories.isNotEmpty())
                    categoryLabel.visibility = View.VISIBLE
                else
                    categoryLabel.visibility = View.INVISIBLE

                load.hide()
            }
        }
    }

    private val failToLoadData : () -> Unit = {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val alert = AlertDialog.Builder(context!!)
                    .setMessage(context!!.getString(R.string.message_fail_load_data))
                    .setPositiveButton(context!!.getString(R.string.buuton_close), null).create()
                alert.show()
                load.hide()
            }
        }
    }

    private val noConnection : () -> Unit = {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val alert = AlertDialog.Builder(context!!)
                    .setMessage(context!!.getString(R.string.message_no_connection))
                    .setPositiveButton(context!!.getString(R.string.buuton_close), null).create()
                alert.show()
                load.hide()
            }
        }
    }

    private val noResult : () -> Unit = {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val arguments = Bundle().apply {
                    putSerializable(Search::class.java.canonicalName, Search().emptySeach())
                }
                (activity!! as MainActivity).intent?.putExtras(arguments)
                load.hide()
                view?.findNavController()?.popBackStack()
            }
        }
    }
}