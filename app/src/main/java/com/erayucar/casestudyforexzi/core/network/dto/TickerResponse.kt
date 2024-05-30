package com.erayucar.casestudyforexzi.core.network.dto

data class TickerResponse(
    val `data`: List<Data>,
    val is_login: Boolean,
    val status: Boolean
)