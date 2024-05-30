package com.erayucar.casestudyforexzi.core.network.di

import com.erayucar.casestudyforexzi.core.common.Constants.API_BASE_URL
import com.erayucar.casestudyforexzi.core.common.Constants.SOCKET_BASE_URL
import com.erayucar.casestudyforexzi.core.network.source.rest.ExziRestApi
import com.erayucar.casestudyforexzi.core.network.source.rest.ExziRestSocket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideSocketRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SOCKET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExziRestSocket(retrofit: Retrofit): ExziRestSocket {
        return retrofit.create(ExziRestSocket::class.java)
    }


    @Provides
    @Singleton
    @Named("ApiRetrofit")
    fun provideApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideExziRestApi(@Named("ApiRetrofit") retrofit: Retrofit): ExziRestApi {
        return retrofit.create(ExziRestApi::class.java)
    }


}