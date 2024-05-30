package com.erayucar.casestudyforexzi.core.network.source.rest

import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.GraphHistResponseDto
import com.erayucar.casestudyforexzi.core.network.dto.TickerResponse
import retrofit2.Response
import javax.inject.Inject

class RestDataSourceImpl @Inject constructor(
    private val restApi: ExziRestApi,
    private val restSocket: ExziRestSocket
) : RestDataSource {
    override suspend fun getTicker(): Response<TickerResponse> {
        return restApi.getTicker()
    }

    override suspend fun getGraphHist(pairName:String, timeStamp: Long): Response<GraphHistResponseDto> {
      return restSocket.getGraphHist(pairName = pairName, timestamp =  timeStamp)
    }

    override suspend fun getBookList(id :String): Response<BookListResponse> {
        return restSocket.getBookList(id)
    }
}