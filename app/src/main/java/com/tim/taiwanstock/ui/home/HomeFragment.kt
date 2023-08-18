package com.tim.taiwanstock.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tim.taiwanstock.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        viewModel.liveData.observe(viewLifecycleOwner) {
            Log.d("TimT", "observe live data: $it")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.stateFlow.collect { state ->
                Log.d("TimT", "observe state flow: $state")
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.dataSharedFlow.collect{ state ->
                Log.d("TimT", "observe data shared flow: $state")
            }
        }



        binding.myButton.setOnClickListener {
            viewModel.updateLiveDataAndStateFlow()
        }
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLiveDataAndFlowData()
    }
    fun printLiveDataAndFlowData() {
        Log.d("TimT", "live data: ${viewModel.liveData.value}")
        Log.d("TimT", "flow data: ${viewModel.stateFlow.value}")
        Log.d("TimT", "test data: ${viewModel.test}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}