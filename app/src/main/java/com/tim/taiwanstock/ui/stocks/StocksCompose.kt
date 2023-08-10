package com.tim.taiwanstock.ui.stocks

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tim.taiwanstock.R
import com.tim.taiwanstock.network.StockDataResponse
import com.tim.taiwanstock.network.closingPrice
import com.tim.taiwanstock.network.getInfo
import com.tim.taiwanstock.network.getStockId
import com.tim.taiwanstock.ui.stocks.company.InputData
import com.tim.taiwanstock.ui.stocks.company.IntradayInfo
import com.tim.taiwanstock.ui.stocks.company.StockChart
import com.tim.taiwanstock.ui.stocks.compose.BasicsCodelabTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun StockNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "fake") {
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

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
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

@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

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
            Text(text = "Hello, ")
            Text(
                text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}
//
//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "DefaultPreviewDark"
//)
//@Preview(showBackground = true, widthDp = 320)
//@Composable
//fun DefaultPreview() {
//    BasicsCodelabTheme {
//        Greetings()
//    }
//}
//
//@Preview(showBackground = true, widthDp = 320, heightDp = 320)
//@Composable
//fun OnboardingPreview() {
//    BasicsCodelabTheme {
//        OnboardingScreen(onContinueClicked = {})
//    }
//}
//
//@Preview
//@Composable
//fun MyAppPreview() {
//    BasicsCodelabTheme {
//        MyApp(Modifier.fillMaxSize())
//    }
//}