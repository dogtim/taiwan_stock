package com.tim.taiwanstock.ui.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tim.taiwanstock.ui.stocks.StockNavHost

@Preview
@Composable
fun StockNavHostPreview() {
    StockNavHost(modifier = Modifier.fillMaxSize())
}