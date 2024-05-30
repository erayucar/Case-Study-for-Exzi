package com.erayucar.casestudyforexzi.core.domain

import com.erayucar.casestudyforexzi.core.data.MarketRepository
import javax.inject.Inject

class GetBookListUseCase @Inject constructor(private val marketRepository: MarketRepository){
    suspend operator fun invoke(id : String) = marketRepository.getBookList(id)
}