package com.tim.taiwanstock.ui.stocks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.tim.taiwanstock.ui.stocks.company.InputData
import com.tim.taiwanstock.ui.stocks.company.IntradayInfo
import com.tim.taiwanstock.ui.stocks.company.SmoothLineGraph
import com.tim.taiwanstock.ui.stocks.compose.BasicsCodelabTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO:
// Show the buy or sell wording
class StocksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BasicsCodelabTheme {
                    //StockNavHost(modifier = Modifier.fillMaxSize())
                    val inputJson = """
        {"data":[["112/08/01","567.00"],["112/08/02","561.00"],["112/08/04","554.00"],["112/08/07","558.00"],["112/08/08","552.00"],["112/08/09","554.00"]]}
    """.trimIndent()

                    val gson = Gson()
                    val parsedData = gson.fromJson(inputJson, InputData::class.java)

                    val dateFormatter = DateTimeFormatter.ofPattern("yyy/MM/dd")

                    val list = parsedData.data.map { entry ->
                        var date = LocalDate.parse(entry[0], dateFormatter)
                        date = date.plusYears(1911L)
                        val close = entry[1].toDouble()
                        IntradayInfo(date, close)
                    }
                    SmoothLineGraph(list)
                    //StockNavHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Tim", "onViewCreated")
    }

}