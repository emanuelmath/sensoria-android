package com.example.sensoria.core.di

import android.content.Context
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.data.local.dataStore
import com.example.sensoria.data.remote.AuthApi
import com.example.sensoria.data.remote.DeviceApi
import com.example.sensoria.data.remote.RetrofitClient
import com.example.sensoria.data.repositories.AuthRepositoryImpl
import com.example.sensoria.data.repositories.DeviceRepositoryImpl
import com.example.sensoria.domain.repositories.AuthRepository
import com.example.sensoria.domain.repositories.DeviceRepository
import com.example.sensoria.domain.usecases.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferences(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthApi(authPreferences: AuthPreferences): AuthApi {
        return RetrofitClient.createAuthApi(authPreferences)
    }

    @Provides
    @Singleton
    fun provideDeviceApi(authPreferences: AuthPreferences): DeviceApi {
        return RetrofitClient.createDeviceApi(authPreferences)
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(api: DeviceApi): DeviceRepository {
        return DeviceRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, preferences: AuthPreferences): AuthRepository {
        return AuthRepositoryImpl(api, preferences)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetDevicesUseCase(repository: DeviceRepository): com.example.sensoria.domain.usecases.GetDevicesUseCase {
        return com.example.sensoria.domain.usecases.GetDevicesUseCase(repository)
    }
}