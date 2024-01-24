package com.example.ProjetAndroid.ViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.ProjetAndroid.Model.VnListRepositoryImpl
import com.example.ProjetAndroid.Model.database.AppDatabase
import com.example.ProjetAndroid.Model.api.ApiService
import com.example.ProjetAndroid.Model.DTOs.VnListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindVnListRepository(impl: VnListRepositoryImpl): VnListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideDb(
            application: Application
        ): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        @ApplicationScope
        @Provides
        fun provideService(): ApiService {
            return ApiService.create()
        }

        @ApplicationScope
        @Provides
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return application.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        }
    }
}