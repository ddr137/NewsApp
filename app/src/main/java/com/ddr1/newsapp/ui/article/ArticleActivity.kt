package com.ddr1.newsapp.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import com.ddr1.newsapp.R
import com.ddr1.newsapp.databinding.ActivityArticleBinding
import com.ddr1.newsapp.databinding.CustomToolbarBinding
import com.ddr1.newsapp.models.ArticleModel
import com.ddr1.newsapp.models.SourceModel
import com.ddr1.newsapp.ui.detail.DetailArticleActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val articleModule = module {
    factory { ArticleActivity() }
}

class ArticleActivity : AppCompatActivity() {

    private val binding by lazy { ActivityArticleBinding.inflate(layoutInflater) }
    private lateinit var bindingToolbar: CustomToolbarBinding
    private val articleViewModel: ArticleViewModel by viewModel()
    private val sourceName by lazy {
        intent.getSerializableExtra("intent_source") as SourceModel.Source
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindingToolbar = binding.toolbar
        setSupportActionBar(bindingToolbar.container)
        supportActionBar!!.apply {
            title = articleViewModel.titleToolbar
            setDisplayHomeAsUpEnabled(true)
        }

        articleViewModel.sources = sourceName.id
        binding.tvCategory.text = sourceName.category
        binding.tvSource.text = sourceName.name

        firstLoad()
        articleViewModel.loading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

        articleViewModel.loadingMore.observe(this) {
            if (it) {
                binding.progressBottom.visibility = View.VISIBLE
            } else {
                binding.progressBottom.visibility = View.GONE
            }
        }

        articleViewModel.articleNews.observe(this) {
            binding.rvArticle.adapter = articleAdapter
            if (articleViewModel.page == 1) articleAdapter.clear()
            articleAdapter.add(it.articles)
        }

        articleViewModel.message.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.scroll.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0)!!.measuredHeight - v.measuredHeight) {
                if (articleViewModel.page <= articleViewModel.total && articleViewModel.loadingMore.value == false) articleViewModel.fetchNewsArticle()
            }
        }
    }

    private fun firstLoad() {
        binding.scroll.scrollTo(0, 0)
        articleViewModel.page = 1
        articleViewModel.total = 1
        articleViewModel.fetchNewsArticle()
    }

    private val articleAdapter by lazy {
        ArticleAdapter(arrayListOf(), object : ArticleAdapter.OnAdapterListener {
            override fun onClick(articleModel: ArticleModel.Article) {
                startActivity(
                    Intent(this@ArticleActivity, DetailArticleActivity::class.java)
                        .putExtra("intent_article", articleModel)
                )
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.menu_search, menu)
        bindingToolbar.container.inflateMenu(R.menu.menu_search)
        val menu = binding.toolbar.container.menu
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                firstLoad()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    articleViewModel.query = it
                    if (articleViewModel.query == "") {
                        articleAdapter.clear()
                        firstLoad()
                    }
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}