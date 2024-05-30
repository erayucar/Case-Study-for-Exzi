package com.erayucar.casestudyforexzi.core.data

import com.erayucar.casestudyforexzi.core.common.ResponseState
import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.TickerResponse
import com.erayucar.casestudyforexzi.core.network.source.rest.RestDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val restDataSource : RestDataSource) : MarketRepository {
    override suspend fun getTicker(): Flow<ResponseState<TickerResponse>>  = flow{
        emit(ResponseState.Loading)
        val response = restDataSource.getTicker()
        emit(ResponseState.Success(response.body()!!))
    }.catch {
        emit(ResponseState.Error(it.message.orEmpty()))
    }

    override suspend fun getGraphHist(
        pairName: String,
        timeStamp: Long
    ): Flow<ResponseState<List<CandleEntry>>> = flow {
        emit(ResponseState.Loading)
        val response = restDataSource.getGraphHist(pairName, timeStamp)
        emit(ResponseState.Success(response.mapTo { it.toCandleEntries() }))
    }.catch {
        emit(ResponseState.Error(it.message.orEmpty()))
    }

    override suspend fun getBookList(id: String): Flow<ResponseState<BookListResponse>> = flow{

        emit(ResponseState.Loading)
        val response = restDataSource.getBookList(id)
        emit(ResponseState.Success(response.body()!!))
    }.catch {
        emit(ResponseState.Error(it.message.orEmpty()))
    }
}