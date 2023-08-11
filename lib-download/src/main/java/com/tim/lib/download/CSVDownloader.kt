package com.tim.lib.download

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Saves the image to a permanent file
 */
private const val TAG = "SaveImageToFileWorker"
class CSVDownloader(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        Log.i(TAG, "Saving image")
        sleep()

        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            if (!resourceUri.isNullOrEmpty()) {
                val output = Data.Builder().putString(KEY_IMAGE_URI, resourceUri).build()
                Result.success(output)
            } else {
                Log.e(TAG, "Writing to MediaStore failed")
                Result.failure()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}