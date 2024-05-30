package com.erayucar.casestudyforexzi.core.network.dto

data class BookListResponse(
    val buy: List<Buy>,
    val sell: List<Sell>
)