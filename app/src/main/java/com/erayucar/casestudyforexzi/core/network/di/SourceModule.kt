package com.erayucar.casestudyforexzi.core.network.di

import com.erayucar.casestudyforexzi.core.network.source.rest.RestDataSource
import com.erayucar.casestudyforexzi.core.network.source.rest.RestDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    @Singleton
    abstract fun bindRestDataSource(restDataSourceImpl: RestDataSourceImpl): RestDataSource
}