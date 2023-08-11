package com.tim.lib.download

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.tim.lib.download.network.StockDataApi
import com.tim.lib.download.network.StockDataConstant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "JsonDownloader"
class JsonDownloader(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    companion object {
        const val KEY_DATE = "KEY_DATE"
        const val KEY_STOCK_NO = "KEY_STOCK_NO"
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.twse.com.tw/") // Replace with your base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork: start")
        sleep()
        return try {
            Result.success(getData(inputData.getString(KEY_DATE)!!, inputData.getString(KEY_STOCK_NO)!!))
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun getData(date: String, stockNo: String): Data {
        val apiService: StockDataApi = retrofit.create(StockDataApi::class.java)
        val response = apiService.downloadCsv(date, stockNo)
        val gson = Gson()
        val dataString = gson.toJson(response.data)
        Log.i(TAG, response.toString())
        return Data.Builder()
            .putString(StockDataConstant.KEY_DATE, response.date)
            .putString(StockDataConstant.KEY_DATA, dataString)
            .build()
    }
}