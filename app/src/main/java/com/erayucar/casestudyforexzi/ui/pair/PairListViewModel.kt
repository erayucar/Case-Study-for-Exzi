package com.erayucar.casestudyforexzi.ui.pair

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayucar.casestudyforexzi.core.common.ResponseState
import com.erayucar.casestudyforexzi.core.domain.GetTickerUseCase
import com.erayucar.casestudyforexzi.core.network.dto.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PairListViewModel @Inject constructor(private val getTickerUseCase: GetTickerUseCase) :
    ViewModel() {

    private val _pairList = MutableLiveData(PairListUiState.initial())
    val pairList : LiveData<PairListUiState> = _pairList

    fun getPairList() {
        viewModelScope.launch {
            getTickerUseCase().collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        // Handle loading
                    }

                    is ResponseState.Success -> {
                        _pairList.postValue(PairListUiState(pairList = responseState.data.data))
                    }

                    is ResponseState.Error -> {
                        _pairList.postValue(PairListUiState(isError = true, errorMessage = responseState.message))
                    }
                }
            }
        }
    }

}
data class PairListUiState(
    val pairList: List<Data> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = ""
) {
    companion object {
        fun initial() = PairListUiState(isLoading = true)
    }
}