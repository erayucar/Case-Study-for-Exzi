package com.erayucar.casestudyforexzi.core.data.di

import com.erayucar.casestudyforexzi.core.data.MarketRepository
import com.erayucar.casestudyforexzi.core.data.MarketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarketRepository(marketRepositoryImpl: MarketRepositoryImpl): MarketRepository
}