package com.erayucar.casestudyforexzi.core.data

import com.erayucar.casestudyforexzi.core.common.ResponseState
import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.TickerResponse
import kotlinx.coroutines.flow.Flow

interface MarketRepository {

    suspend fun getTicker(): Flow<ResponseState<TickerResponse>>

    suspend fun getGraphHist(pairName: String, timeStamp: Long): Flow<ResponseState<List<CandleEntry>>>

    suspend fun getBookList(id: String):  Flow<ResponseState<BookListResponse>>
}