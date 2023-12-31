package com.tim.lib.download

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tim.lib.download.network.ApiService
import retrofit2.Retrofit

/**
 * Saves the csv to database
 */
private const val TAG = "CSVDownloader"

class CSVDownloader(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.i(TAG, "Start fetching")
        initDownload()
        sleep()
        Log.i(TAG, "Saving image")
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
    val date = "20230811"
    val stockNo = "2330"
    // Failed: java.util.concurrent.ExecutionException: javax.net.ssl.SSLHandshakeException:
    // java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
    private suspend fun initDownload() {
        val apiService: ApiService

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.twse.com.tw/") // Replace with your base URL
            .build()

        apiService = retrofit.create(ApiService::class.java)
        val response = apiService.downloadCsv(date, stockNo)
        Log.i(TAG, response.string())
    }
}