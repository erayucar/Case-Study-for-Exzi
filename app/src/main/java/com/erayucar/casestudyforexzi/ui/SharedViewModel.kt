package com.erayucar.casestudyforexzi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayucar.casestudyforexzi.core.common.ResponseState
import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.erayucar.casestudyforexzi.core.domain.GetBookListUseCase
import com.erayucar.casestudyforexzi.core.domain.GetGraphHistUseCase
import com.erayucar.casestudyforexzi.core.domain.GetTickerUseCase
import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val getGraphHistUseCase: GetGraphHistUseCase,
    private val getTickerUseCase: GetTickerUseCase
) : ViewModel() {


    private val _orderBookList = MutableLiveData(OrderBookUiState.initial())
    val orderBookList: LiveData<OrderBookUiState> = _orderBookList
    private val _graphHistList = MutableLiveData(GraphHistUiState.initial())
    val graphHistList: LiveData<GraphHistUiState> = _graphHistList
    private val _pair = MutableLiveData(PairUiState.initial())
    val pair: LiveData<PairUiState> = _pair


    fun getOrderBook(id: String) {
        viewModelScope.launch {
            getBookListUseCase(id).collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        // Handle loading
                    }

                    is ResponseState.Success -> {
                        _orderBookList.postValue(OrderBookUiState(orderBookList = responseState.data))
                    }

                    is ResponseState.Error -> {
                       _orderBookList.postValue(OrderBookUiState(isError = true, errorMessage = responseState.message))
                    }
                }
            }
        }
    }

    fun getGraphHist(pairName: String, timeStamp: Long) {
        viewModelScope.launch {
            getGraphHistUseCase(pairName, timeStamp).collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        // Handle loading
                    }

                    is ResponseState.Success -> {
                        _graphHistList.postValue(GraphHistUiState(graphHistList = responseState.data))
                    }

                    is ResponseState.Error -> {
                       _graphHistList.postValue(GraphHistUiState(isError = true, errorMessage = responseState.message))
                    }
                }
            }
        }
    }

    fun getPair(pairID : String) {
        viewModelScope.launch {
            getTickerUseCase().collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        // Handle loading
                    }

                    is ResponseState.Success -> {
                        _pair.postValue(PairUiState(pair =  responseState.data.data.find { it._id == pairID }))
                    }

                    is ResponseState.Error -> {
                        _pair.postValue(PairUiState(isError = true, errorMessage = responseState.message))
                    }
                }
            }
        }
    }
}

data class OrderBookUiState(
    val orderBookList: BookListResponse? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = OrderBookUiState(isLoading = true)
    }
}

data class PairUiState(
    val pair: Data? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = PairUiState(isLoading = true)
    }
}

data class GraphHistUiState(
    val graphHistList: List<CandleEntry> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = GraphHistUiState(isLoading = true)
    }
}
