package com.ddr1.newsapp.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.annotation.ExperimentalCoilApi
import com.ddr1.newsapp.databinding.ItemArticleBinding
import com.ddr1.newsapp.models.ArticleModel
import com.lazday.news.utils.DateUtil
import com.lazday.news.utils.loadImage

class ArticleAdapter(
    private val articles: ArrayList<ArticleModel.Article>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        @OptIn(ExperimentalCoilApi::class)
        fun bind(articleModel: ArticleModel.Article) {
            val format = DateUtil()
            loadImage(binding.image, articleModel.urlToImage)
            binding.title.text = articleModel.title
            binding.publishedAt.text = format.dateFormat(articleModel.publishedAt)

        }
    }

    interface OnAdapterListener {
        fun onClick(articleModel: ArticleModel.Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            listener.onClick(article)
        }
    }

    override fun getItemCount() = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(data: List<ArticleModel.Article>) {
        articles.addAll(data)
        notifyItemRangeInserted((articles.size - data.size), data.size)
    }

    fun clear() {
        articles.clear()
        notifyDataSetChanged()
    }
}