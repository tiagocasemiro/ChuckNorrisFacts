package com.chucknorrisfacts.view

import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chucknorrisfacts.R
import com.chucknorrisfacts.view.FactsAdapter.FactViewHolder
import com.domain.Search
import kotlinx.android.synthetic.main.adapter_fact_item.view.*

class FactsAdapter(private val search: Search): RecyclerView.Adapter<FactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        return FactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_fact_item, parent, false))
    }

    override fun getItemCount(): Int {
        return search.total?.toInt() ?: run { 0 }
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        val limitToChangeFont = 80
        holder.category.text = search.result?.get(position)?.category?.name
        holder.description.text = search.result?.get(position)?.value
        if(search.result?.get(position)?.value?.length!! > limitToChangeFont) {
            holder.description.setTextSize(TypedValue.COMPLEX_UNIT_SP, holder.itemView.context.resources.getDimension(R.dimen.font_size_small))
        } else {
            holder.description.setTextSize(TypedValue.COMPLEX_UNIT_SP, holder.itemView.context.resources.getDimension(R.dimen.font_size_large))
        }
        holder.share.setOnClickListener{
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/html"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, holder.itemView.context.getString(R.string.app_name))
                shareIntent.putExtra(Intent.EXTRA_TEXT, search.result?.get(position)?.url)
                holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Choose one"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class FactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.description
        val category: TextView = itemView.category
        val share: ImageView = itemView.share
    }
}