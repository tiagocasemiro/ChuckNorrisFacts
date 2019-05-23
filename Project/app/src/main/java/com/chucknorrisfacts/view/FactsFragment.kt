package com.chucknorrisfacts.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.chucknorrisfacts.R
import kotlinx.android.synthetic.main.fragment_facts.view.*

class FactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_facts, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = ""

        view.click.setOnClickListener {
            view.findNavController().navigate(R.id.action_factsFragment_to_searchFragment)
        }

        return view
    }


}
