package com.tim.taiwanstock

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tim.lib.download.Constants
import com.tim.lib.download.DownloadViewModel
import com.tim.lib.download.DownloadViewModelFactory
import com.tim.lib.download.HelloLib
import com.tim.lib.download.network.StockDataConstant
import com.tim.taiwanstock.databinding.ActivityMainBinding
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_stocks
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        printHelloLib()
    }

    private val viewModel: DownloadViewModel by viewModels { DownloadViewModelFactory(application) }
    fun printHelloLib() {
        HelloLib().sayHello()
        viewModel.workInfo.observe(this@MainActivity) { info ->
            if (info.size == 0) return@observe else onStateChange(info[0])
        }
        viewModel.testRun()
    }

    private fun onStateChange(info: WorkInfo) {
        val finished = info.state.isFinished

        with(binding) {
            if (!finished) {
                Log.i("Tim", "Show loading")
            } else {
                Log.i("Tim", "hide loading")
            }
        }
        val outputData = info.outputData
        outputData.getString(StockDataConstant.KEY_DATE)?.let {
            Log.i("Tim", "it = $it")
        }
        outputData.getString(StockDataConstant.KEY_DATA)?.let {
            val gson = Gson()
            val listType: Type = object : TypeToken<List<List<String>>>() {}.type
            val convertedData: List<List<String>> = gson.fromJson(it, listType)
            Log.i("Tim", "convertedData = $convertedData")
        }
    }
}