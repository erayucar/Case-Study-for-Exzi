package com.erayucar.casestudyforexzi.core.network.dto

data class Main(
    val cap: Double,
    val cap_f: String,
    val circulating_supply: String,
    val decimal: Int,
    val id: Int,
    val iso3: String,
    val maximum_supply: String,
    val name: String,
    val rate_btc: Int,
    val rate_eth: Int,
    val rate_usd: Long,
    val rate_usdt: Long,
    val total_supply: String
)