package com.ddr1.newsapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ddr1.newsapp.databinding.ItemCategoryBinding
import com.ddr1.newsapp.models.CategoryModel

class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val items = arrayListOf<TextView>()

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnAdapterListener {
        fun onClick(categoryModel: CategoryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.tvCategory.text = category.name
        holder.itemView.setOnClickListener {
            listener.onClick(category)
        }
    }

    override fun getItemCount() = categories.size
}