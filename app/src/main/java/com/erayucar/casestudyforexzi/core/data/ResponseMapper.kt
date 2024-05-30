package com.erayucar.casestudyforexzi.core.data

import com.erayucar.casestudyforexzi.core.data.model.CandleEntry
import com.erayucar.casestudyforexzi.core.network.dto.GraphHistResponseDto
import retrofit2.Response

typealias graphHistResponse = Response<GraphHistResponseDto>


fun graphHistResponse.toCandleEntries(): List<CandleEntry> {
    return body()!!.mapIndexed {index, it ->
        CandleEntry(
            x = index.toFloat(),
            high = it.high_f.toFloat(),
            low = it.low_f.toFloat(),
            open = it.open_f.toFloat(),
            close = it.close_f.toFloat()
        )
    }
}
