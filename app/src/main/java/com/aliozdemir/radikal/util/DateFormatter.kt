package com.aliozdemir.radikal.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatPublishedDate(dateString: String?): String {
    if (dateString == null) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.US)
        outputFormat.timeZone = TimeZone.getDefault()

        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        e.printStackTrace()
        dateString
    }
}