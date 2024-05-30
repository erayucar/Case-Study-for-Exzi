package com.erayucar.casestudyforexzi.core.network.source.rest

import com.erayucar.casestudyforexzi.core.network.dto.BookListResponse
import com.erayucar.casestudyforexzi.core.network.dto.GraphHistResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExziRestSocket {

    @GET("book/list")
    suspend fun getBookList(
        @Query("pair_id") id: String,
        @Query("buy") buy: Int = 1,
        @Query("sell") sell: Int = 1
    ): Response<BookListResponse>

    @GET("graph/hist")
    suspend fun getGraphHist(
        @Query("t") pairName: String,
        @Query("r") resolution: String = "D",
        @Query("limit") limit: Int = 5000,
        @Query("end") timestamp: Long
    ): Response<GraphHistResponseDto>
}