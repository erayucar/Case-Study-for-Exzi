package com.erayucar.casestudyforexzi.core.domain

import com.erayucar.casestudyforexzi.core.data.MarketRepository
import javax.inject.Inject

class GetGraphHistUseCase @Inject constructor(private val marketRepository: MarketRepository) {
    suspend operator fun invoke(pairName: String, timeStamp: Long) =
        marketRepository.getGraphHist(pairName, timeStamp)
}