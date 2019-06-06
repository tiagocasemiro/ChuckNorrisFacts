package com.chucknorrisfacts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chucknorrisfacts.R
import com.chucknorrisfacts.configuration.hideBackNavegation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.domain.Search
import kotlinx.android.synthetic.main.fragment_facts.view.*


class FactsFragment : Fragment() {

    private var state = State.VISIBLE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_facts, container, false)
        hideBackNavegation()
        view.search.setOnClickListener {
            view.findNavController().navigate(R.id.action_factsFragment_to_searchFragment)
        }
        (activity!! as MainActivity).intent?.extras?.let { bundle ->
            if(bundle.containsKey(Search::class.java.canonicalName)) {
                val search = bundle.getSerializable(Search::class.java.canonicalName)!! as Search
                if(search.total != null && search.total!! > 0) {
                    view.facts.adapter = FactsAdapter(search)
                    showListWithFacts(view!!)
                    bundle.remove(Search::class.java.canonicalName)
                } else {
                    showEmptyMessage(view!!)
                }
            } else {
                hideAll(view!!)
            }
        }?: run {
            hideAll(view!!)
        }
        view.facts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if(state == State.VISIBLE) {
                        YoYo.with(Techniques.SlideOutDown).duration(200).playOn(view.search)
                        state = State.HIDDEN
                    }
                } else {
                    if(state == State.HIDDEN) {
                        YoYo.with(Techniques.SlideInUp).duration(200).playOn(view.search)
                        state = State.VISIBLE
                    }
                }
            }
        })

        return view
    }

    private fun showListWithFacts(view: View) {
        view.facts!!.visibility = View.VISIBLE
        view.noResult.visibility = View.INVISIBLE
    }

    private fun showEmptyMessage(view: View) {
        view.facts!!.visibility = View.INVISIBLE
        view.noResult.visibility = View.VISIBLE
    }

    private fun hideAll(view: View) {
        view.facts!!.visibility = View.INVISIBLE
        view.noResult.visibility = View.INVISIBLE
    }

    enum class State {
        HIDDEN, VISIBLE;
    }
}
