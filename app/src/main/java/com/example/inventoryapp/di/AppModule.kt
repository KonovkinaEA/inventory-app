package com.example.inventoryapp.di

import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun provideRepository(repository: RepositoryImpl): Repository
}
