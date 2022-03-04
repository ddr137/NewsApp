package com.ddr1.newsapp.ui.source

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import com.ddr1.newsapp.R
import com.ddr1.newsapp.databinding.ActivitySourceBinding
import com.ddr1.newsapp.databinding.CustomToolbarBinding
import com.ddr1.newsapp.models.CategoryModel
import com.ddr1.newsapp.models.SourceModel
import com.ddr1.newsapp.ui.article.ArticleActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import timber.log.Timber

val sourceModule = module {
    factory { SourceActivity() }
}

class SourceActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySourceBinding.inflate(layoutInflater) }
    private lateinit var bindingToolbar: CustomToolbarBinding
    var count = 5
    private val sourceViewModel: SourceViewModel by viewModel()
    private val categoryName by lazy {
        intent.getSerializableExtra("intent_category") as CategoryModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindingToolbar = binding.toolbar
        setSupportActionBar(bindingToolbar.container)
        supportActionBar!!.apply {
            title = sourceViewModel.titleToolbar
            setDisplayHomeAsUpEnabled(true)
        }

        sourceViewModel.category = categoryName.name
        binding.tvCategory.text = categoryName.name

        firstLoad()
        sourceViewModel.loading.observe(this) {
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }

        sourceViewModel.loadingMore.observe(this) {
            if (it) {
                binding.progressBottom.visibility = View.VISIBLE
            } else {
                binding.progressBottom.visibility = View.GONE
            }
        }

        sourceViewModel.sourceNews.observe(this) {
            val data = if (it.sources.size < 6) it.sources
            else it.sources.slice(0..count)
            binding.rvSource.adapter = sourceAdapter
            sourceAdapter.add(data)
        }

        sourceViewModel.message.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.scroll.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0)!!.measuredHeight - v.measuredHeight) {
                count++
                sourceViewModel.fetchNewsSource()
            }
        }

    }

    private fun firstLoad() {
        binding.scroll.scrollTo(0, 0)
        sourceViewModel.fetchNewsSource()
    }

    private val sourceAdapter by lazy {
        SourceAdapter(object : SourceAdapter.OnAdapterListener {
            override fun onClick(sourceModel: SourceModel.Source) {
                startActivity(
                    Intent(this@SourceActivity, ArticleActivity::class.java)
                        .putExtra("intent_source", sourceModel)
                )
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        bindingToolbar.container.inflateMenu(R.menu.menu_search)
        val menu = binding.toolbar.container.menu
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.e(sourceAdapter.sources.toString())
                sourceAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sourceAdapter.filter.filter(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}