package com.rafiul.modernarchitectureapproach.di

import com.rafiul.modernarchitectureapproach.services.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder): UserApi = retrofit.build().create(UserApi::class.java)
}