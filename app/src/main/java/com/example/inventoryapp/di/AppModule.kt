package com.example.inventoryapp.di

import android.app.Application
import android.content.Context
import com.example.inventoryapp.data.Repository
import com.example.inventoryapp.data.RepositoryImpl
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface AppModule {

    @ViewModelScoped
    @Binds
    fun provideRepository(repository: RepositoryImpl): Repository

    companion object {

        @ViewModelScoped
        @Provides
        fun provideBarCodeScanner(
            context: Context,
            options: GmsBarcodeScannerOptions
        ): GmsBarcodeScanner {
            return GmsBarcodeScanning.getClient(context, options)
        }

        @ViewModelScoped
        @Provides
        fun provideBarCodeOptions(): GmsBarcodeScannerOptions {
            return GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        }

        @ViewModelScoped
        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }
    }
}
