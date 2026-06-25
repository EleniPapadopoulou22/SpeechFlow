package com.eleni.speechflow.util

import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale("el", "GR"))
    return sdf.format(Date(timestamp))
}

fun formatDateHeader(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply { time = date }

    return when {
        isSameDay(now, target) -> "Σήμερα"
        isYesterday(now, target) -> "Χθες"
        else -> {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("el", "GR"))
            sdf.format(date)
        }
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun isYesterday(now: Calendar, target: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply {
        time = now.time
        add(Calendar.DAY_OF_YEAR, -1)
    }
    return isSameDay(yesterday, target)
}
