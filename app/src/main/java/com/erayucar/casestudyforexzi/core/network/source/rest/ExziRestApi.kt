package com.erayucar.casestudyforexzi.core.network.source.rest

import com.erayucar.casestudyforexzi.core.network.dto.TickerResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExziRestApi {



    @GET("api/default/ticker")
    suspend fun getTicker(): Response<TickerResponse>

}