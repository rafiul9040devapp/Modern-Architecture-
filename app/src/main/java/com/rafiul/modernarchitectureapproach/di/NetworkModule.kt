package com.rafiul.modernarchitectureapproach.di

import com.rafiul.baselib.NativeLib
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getURL(): String {
        return NativeLib().stringFromJNI()
    }

    @Singleton
    @Provides
    fun providesRetrofit(url: String): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
    }

}