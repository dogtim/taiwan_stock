package com.tim.taiwanstock.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.tim.taiwanstock.ui.stocks.StockNavHost
import com.tim.taiwanstock.ui.stocks.company.InputData
import com.tim.taiwanstock.ui.stocks.company.IntradayInfo
import com.tim.taiwanstock.ui.stocks.company.SmoothLineGraph
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun StockNavHostPreview() {
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
}