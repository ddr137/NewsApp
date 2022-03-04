package com.ddr1.newsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ddr1.newsapp.network.networkModule
import com.ddr1.newsapp.ui.article.articleModule
import com.ddr1.newsapp.ui.article.articleRepository
import com.ddr1.newsapp.ui.article.articleViewModel
import com.ddr1.newsapp.ui.category.categoryModule
import com.ddr1.newsapp.ui.category.categoryRepository
import com.ddr1.newsapp.ui.category.categoryViewModel
import com.ddr1.newsapp.ui.source.sourceModule
import com.ddr1.newsapp.ui.source.sourceRepository
import com.ddr1.newsapp.ui.source.sourceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@NewsApp)
            modules(
                networkModule,
                categoryRepository,
                categoryModule,
                categoryViewModel,
                sourceModule,
                sourceRepository,
                sourceViewModel,
                articleModule,
                articleRepository,
                articleViewModel
            )
        }
    }
}