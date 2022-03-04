package com.ddr1.newsapp.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.ddr1.newsapp.databinding.ActivityCategoryBinding
import com.ddr1.newsapp.databinding.CustomToolbarBinding
import com.ddr1.newsapp.models.ArticleModel
import com.ddr1.newsapp.models.CategoryModel
import com.ddr1.newsapp.ui.article.ArticleAdapter
import com.ddr1.newsapp.ui.detail.DetailArticleActivity
import com.ddr1.newsapp.ui.source.SourceActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import timber.log.Timber

val categoryModule = module {
    factory { CategoryActivity() }
}

class CategoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCategoryBinding.inflate(layoutInflater) }
    private lateinit var bindingToolbar: CustomToolbarBinding
    private val categoryViewModel: CategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindingToolbar = binding.toolbar
        bindingToolbar.textTitle.text = categoryViewModel.titleToolbar

        binding.rvCategory.adapter = categoryAdapter
        categoryViewModel.category.observe(this) {
            Timber.e(it)
        }

        firstLoad()
        categoryViewModel.loading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

        categoryViewModel.loadingMore.observe(this) {
            if (it) {
                binding.progressBottom.visibility = View.VISIBLE
            } else {
                binding.progressBottom.visibility = View.GONE
            }
        }

        categoryViewModel.articleNews.observe(this) {
            binding.rvArticle.adapter = articleAdapter
            if (categoryViewModel.page == 1) articleAdapter.clear()
            articleAdapter.add(it.articles)
        }

        categoryViewModel.message.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.scroll.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0)!!.measuredHeight - v.measuredHeight) {
                if (categoryViewModel.page <= categoryViewModel.total && categoryViewModel.loadingMore.value == false) categoryViewModel.fetchTopNewsArticle()
            }
        }
    }

    private fun firstLoad() {
        binding.scroll.scrollTo(0, 0)
        categoryViewModel.page = 1
        categoryViewModel.total = 1
        categoryViewModel.fetchTopNewsArticle()
    }

    private val categoryAdapter by lazy {
        CategoryAdapter(categoryViewModel.categories, object : CategoryAdapter.OnAdapterListener {
            override fun onClick(categoryModel: CategoryModel) {
                startActivity(
                    Intent(this@CategoryActivity, SourceActivity::class.java)
                        .putExtra("intent_category", categoryModel)
                )
            }
        })
    }

    private val articleAdapter by lazy {
        ArticleAdapter(arrayListOf(), object : ArticleAdapter.OnAdapterListener {
            override fun onClick(articleModel: ArticleModel.Article) {
                startActivity(
                    Intent(this@CategoryActivity, DetailArticleActivity::class.java)
                        .putExtra("intent_article", articleModel)
                )
            }
        })
    }
}