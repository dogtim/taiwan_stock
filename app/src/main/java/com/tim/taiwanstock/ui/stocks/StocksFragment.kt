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
import androidx.fragment.app.viewModels
import com.tim.taiwanstock.ui.stocks.compose.BasicsCodelabTheme

// TODO:
// 2. List the top 10 weighted stock or companies
// 3. Show the buy or sell wording
class StocksFragment : Fragment() {

    private val viewModel: StocksViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BasicsCodelabTheme {
                    Stock(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Tim", "onViewCreated")
    }

}