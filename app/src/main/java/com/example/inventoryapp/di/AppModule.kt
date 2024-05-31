package com.example.inventoryapp.di

import android.content.Context
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.RepositoryImpl
import com.example.inventoryapp.data.api.AndroidDownloader
import com.example.inventoryapp.data.api.ApiService
import com.example.inventoryapp.data.datastore.DataStoreManager
import com.example.inventoryapp.data.db.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        fun provideAndroidDownloader(@ApplicationContext context: Context) =
            AndroidDownloader(context)

        @Singleton
        @Provides
        fun provideItemDao(database: AppDatabase) = database.getTodoItemDao()

        @Singleton
        @Provides
        fun provideDeleteDao(database: AppDatabase) = database.getDeleteDao()

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

        @Singleton
        @Provides
        fun provideApiService(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            dataStoreManager: DataStoreManager
        ): ApiService {
            return Retrofit.Builder()
                .baseUrl("http://" + runBlocking {
                    dataStoreManager.getIpAddress() ?: ""
                } + "/api/v1/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(ApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }

        @Singleton
        @Provides
        fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        @Provides
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }
    }
}
