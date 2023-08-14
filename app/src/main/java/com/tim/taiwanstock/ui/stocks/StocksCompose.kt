package com.tim.taiwanstock.ui.stocks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tim.taiwanstock.network.StockDataResponse
import com.tim.taiwanstock.network.closingPrice
import com.tim.taiwanstock.network.getInfo
import com.tim.taiwanstock.network.getStockId
import com.tim.taiwanstock.ui.stocks.company.InputData
import com.tim.taiwanstock.ui.stocks.company.IntradayInfo
import com.tim.taiwanstock.ui.stocks.company.StockChart
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StockNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "stocks") {
        composable("stocks") {
            Stock(modifier = modifier, onStockClick = { stockId ->
                navController.navigate("stock/$stockId")
            })
        }
        composable(
            //"stock/{stockId}",
            "fake",
            arguments = listOf(navArgument("stockId") {
                type = NavType.StringType
            })
        ) {
            val inputJson = """
        {"data":[["112/08/01","567.00"],["112/08/02","561.00"],["112/08/04","554.00"],["112/08/07","558.00"],["112/08/08","552.00"],["112/08/09","554.00"]]}
    """.trimIndent()

            val gson = Gson()
            val parsedData = gson.fromJson(inputJson, InputData::class.java)

            val dateFormatter = DateTimeFormatter.ofPattern("yyy/MM/dd")

            val intradayInfoList = parsedData.data.map { entry ->
                val date = LocalDate.parse(entry[0], dateFormatter)
                val close = entry[1].toDouble()
                IntradayInfo(date, close)
            }
            StockChart(infos = intradayInfoList, modifier = modifier)
        }
    }
}

@Composable
fun Stock(modifier: Modifier = Modifier,
          onStockClick: (String) -> Unit = {},
          viewModel: StocksViewModel = StocksViewModel()) {

    viewModel.fetchItems()
    val itemList: List<StockDataResponse> by viewModel.itemList.collectAsState()

    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = itemList) { response ->
            StockInfo(response = response, onStockClick = onStockClick )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockInfo(response: StockDataResponse, onStockClick: (String) -> Unit = {},) {
    val name = response.getInfo()
    val price = response.closingPrice()

    Card(
        onClick = {
            onStockClick(response.getStockId())
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)

    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(text = name)
                Text(
                    text = price, style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

            }
        }
    }
}