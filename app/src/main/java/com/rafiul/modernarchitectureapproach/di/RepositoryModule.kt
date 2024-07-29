package com.rafiul.modernarchitectureapproach.di

import com.rafiul.modernarchitectureapproach.repositories.UserRepository
import com.rafiul.modernarchitectureapproach.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWithUserRepository(userRepositoryImpl: UserRepositoryImpl):UserRepository
}