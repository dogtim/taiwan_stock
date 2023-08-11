package com.tim.lib.download

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.tim.lib.download.database.AppDatabase
import com.tim.lib.download.network.ApiService
import com.tim.lib.download.network.StockDataApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "JsonDownloader"
class JsonDownloader(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
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
    private suspend fun initDownload() {
        val apiService: StockDataApi

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.twse.com.tw/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(StockDataApi::class.java)
        val response = apiService.downloadCsv(date, stockNo)
        Log.i(TAG, response.toString())

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}