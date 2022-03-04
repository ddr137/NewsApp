package com.lazday.news.utils

import android.widget.ImageView
import coil.annotation.ExperimentalCoilApi
import coil.load
import coil.transition.CrossfadeTransition
import com.ddr1.newsapp.R

@ExperimentalCoilApi
fun loadImage(imageView: ImageView, urlString: String?) {
    urlString?.let {
        imageView.load(urlString) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            transition(CrossfadeTransition())
            error(R.drawable.placeholder)
        }
    }
}