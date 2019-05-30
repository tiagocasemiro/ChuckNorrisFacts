package com.chucknorrisfacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.hideBackNavegation
import com.domain.Search
import kotlinx.android.synthetic.main.fragment_facts.view.*

class FactsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_facts, container, false)
        hideBackNavegation()
        view.click.setOnClickListener {
            view.findNavController().navigate(R.id.action_factsFragment_to_searchFragment)
        }
        (activity!! as MainActivity).intent?.extras?.let { bundle ->
            if(bundle.containsKey(Search::class.java.canonicalName)) {
                val search = bundle.getSerializable(Search::class.java.canonicalName)!! as Search

            }
        }

        return view
    }
}
