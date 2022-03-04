package com.ddr1.newsapp.ui.source

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.ddr1.newsapp.databinding.ItemSourceBinding
import com.ddr1.newsapp.models.SourceModel
import java.util.*

class SourceAdapter(
    var listener: OnAdapterListener,
) : RecyclerView.Adapter<SourceAdapter.ViewHolder>(), Filterable {

    var sources: ArrayList<SourceModel.Source> = ArrayList()
    var sourceListFiltered: ArrayList<SourceModel.Source> = ArrayList()

    class ViewHolder(val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(sourceModel: SourceModel.Source) {
            binding.tvSourceName.text = sourceModel.name
            binding.tvSourceDesc.text = sourceModel.description
            binding.tvSourceURL.text = sourceModel.url
        }
    }

    interface OnAdapterListener {
        fun onClick(sourceModel: SourceModel.Source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSourceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val source = sourceListFiltered[position]
        holder.bind(source)
        holder.itemView.setOnClickListener {
            listener.onClick(source)
        }
    }

    override fun getItemCount() = sourceListFiltered.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(data: List<SourceModel.Source>) {
        sources = data as ArrayList<SourceModel.Source>
        sourceListFiltered = sources
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                sourceListFiltered = if (charString.isEmpty()) sources else {
                    val filteredList = ArrayList<SourceModel.Source>()
                    sources
                        .filter { it.name.lowercase(Locale.getDefault()).contains(constraint!!) }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = sourceListFiltered }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                sourceListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<SourceModel.Source>
                notifyDataSetChanged()
            }

        }
    }

}