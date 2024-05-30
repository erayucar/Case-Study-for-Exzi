package com.erayucar.casestudyforexzi.core.network.dto

data class GraphHistResponseItem(
    val low: Long,
    val high: Long,
    val volume: Long,
    val time: Long,
    val open: Long,
    val close: Long,
    val pair_id: Int,
    val pair: String,
    val low_f: String,
    val high_f: String,
    val open_f: String,
    val close_f: String,
    val volume_f: String

)