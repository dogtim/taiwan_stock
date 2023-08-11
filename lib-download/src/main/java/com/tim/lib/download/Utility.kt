package com.tim.lib.download

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val TAG = "WorkerUtils"
fun sleep() {
    try {
        Thread.sleep(DELAY_TIME_MILLIS, 0)
    } catch (e: InterruptedException) {
        Log.e(TAG, e.message.toString())
    }
}

fun getCurrentDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return LocalDate.now().format(dateFormatter)
}