package com.tim.lib.download

import android.util.Log

private const val TAG = "WorkerUtils"
fun sleep() {
    try {
        Thread.sleep(DELAY_TIME_MILLIS, 0)
    } catch (e: InterruptedException) {
        Log.e(TAG, e.message.toString())
    }
}