package com.yuvarajcode.food_harbor.shared

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

fun formatRelativeTime(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val elapsed = currentTime - timestamp

    // One year in milliseconds (365 days)
    val oneYearMs = 31536000000L

    if (abs(elapsed) >= oneYearMs) {
        // Indian format: "5 Sep 2023, 11:30 AM"
        val dateFormat = SimpleDateFormat("d MMM yyyy, h:mm a", Locale("en", "IN"))
        return dateFormat.format(Date(timestamp))
    }

    val elapsedSeconds = abs(elapsed) / 1000

    val intervals = listOf(
        Pair("month", 2592000L),  // 30 days
        Pair("week", 604800L),    // 7 days
        Pair("day", 86400L),      // 1 day
        Pair("hour", 3600L),      // 1 hour
        Pair("minute", 60L),      // 1 minute
        Pair("second", 1L)
    )

    for ((unit, seconds) in intervals) {
        val count = elapsedSeconds / seconds
        if (count >= 1) {
            return getRelativeTime(count.toInt(), unit)
        }
    }

    return "Just now"
}

private fun getRelativeTime(value: Int, unit: String): String {
    return when {
        value == 1 -> when (unit) {
            "hour" -> "an hour ago"
            "day" -> "yesterday"  // Common Indian English usage
            else -> "a $unit ago"
        }
        unit == "day" && value == 2 -> "day before yesterday" // Common phrase
        else -> "$value ${unit}s ago"
    }
}