package com.example.inventoryapp.di

import android.content.Context
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.RepositoryImpl
import com.example.inventoryapp.data.db.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun provideRepository(repository: RepositoryImpl): Repository

    companion object {

        @Singleton
        @Provides
        fun provideItemDao(database: AppDatabase) = database.getTodoItemDao()

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)
    }
}
