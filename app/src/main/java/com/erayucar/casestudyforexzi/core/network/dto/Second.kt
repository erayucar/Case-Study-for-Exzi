package com.erayucar.casestudyforexzi.core.network.dto

data class Second(
    val decimal: Int,
    val id: Int,
    val iso3: String,
    val name: String,
    val rate_btc: Int,
    val rate_eth: Int,
    val rate_usd: Long,
    val rate_usdt: Long
)