package com.lazday.news.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    fun dateFormat(date: String?): String {
        return if (date.isNullOrEmpty()) ""
        else {
            val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateParse = currentFormat.parse(date)
            val toFormat = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())
            toFormat.format(dateParse)
        }
    }
}