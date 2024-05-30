package com.erayucar.casestudyforexzi.core.network.source.rest

import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.GraphHistResponseDto
import com.erayucar.casestudyforexzi.core.network.dto.TickerResponse
import retrofit2.Response

interface RestDataSource {

    suspend fun getTicker(): Response<TickerResponse>

    suspend fun getGraphHist(pairName: String, timeStamp:Long): Response<GraphHistResponseDto>

    suspend fun getBookList(id: String): Response<BookListResponse>
}