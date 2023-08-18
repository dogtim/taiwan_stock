package com.tim.taiwanstock.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    private val _liveData: MutableLiveData<String>  = MutableLiveData<String>()
    val liveData: LiveData<String>
        get() = _liveData

    private val _stateFlow: MutableStateFlow<String> = MutableStateFlow("")
    val stateFlow: StateFlow<String> = _stateFlow.asStateFlow()
    private val _dataSharedFlow = MutableSharedFlow<String>()

    val dataSharedFlow: SharedFlow<String> = _dataSharedFlow
    var test: String = "init"

    fun updateLiveDataAndStateFlow() {
        _liveData.value = "updateLiveData"
        _stateFlow.value = "updateStateFlow"
        test = "change"
        viewModelScope.launch {
            _dataSharedFlow.emit("updateStateFlow")
        }
    }
    override fun onCleared() {
        super.onCleared()
        Log.d("TimT", "onCleared: ")
        _liveData.value = "onCleared"
    }

}