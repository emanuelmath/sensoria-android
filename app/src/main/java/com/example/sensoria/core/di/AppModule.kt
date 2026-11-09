package com.example.sensoria.core.di

import android.content.Context
import com.example.sensoria.data.local.AuthPreferences
import com.example.sensoria.data.local.dataStore
import com.example.sensoria.data.remote.AuthApi
import com.example.sensoria.data.remote.DeviceApi
import com.example.sensoria.data.remote.RetrofitClient
import com.example.sensoria.data.remote.TicketApi
import com.example.sensoria.data.repositories.AuthRepositoryImpl
import com.example.sensoria.data.repositories.DeviceRepositoryImpl
import com.example.sensoria.data.repositories.TicketRepositoryImpl
import com.example.sensoria.domain.repositories.AuthRepository
import com.example.sensoria.domain.repositories.DeviceRepository
import com.example.sensoria.domain.repositories.TicketRepository
import com.example.sensoria.domain.usecases.CreateTicketUseCase
import com.example.sensoria.domain.usecases.GetTicketDetailUseCase
import com.example.sensoria.domain.usecases.GetTicketsUseCase
import com.example.sensoria.domain.usecases.LoginUseCase
import com.example.sensoria.domain.usecases.ReplyTicketUseCase
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

    @Provides
    @Singleton
    fun provideGetDeviceTelemetryUseCase(repository: DeviceRepository): com.example.sensoria.domain.usecases.GetDeviceTelemetryUseCase {
        return com.example.sensoria.domain.usecases.GetDeviceTelemetryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideTicketApi(authPreferences: AuthPreferences): TicketApi {
        return RetrofitClient.createTicketApi(authPreferences)
    }

    @Provides
    @Singleton
    fun provideTicketRepository(api: TicketApi, authPreferences: AuthPreferences): TicketRepository {
        return TicketRepositoryImpl(api, authPreferences)
    }

    @Provides
    @Singleton
    fun provideGetTicketsUseCase(repository: TicketRepository): GetTicketsUseCase {
        return GetTicketsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTicketDetailUseCase(repository: TicketRepository): GetTicketDetailUseCase {
        return GetTicketDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideReplyTicketUseCase(repository: TicketRepository): ReplyTicketUseCase {
        return ReplyTicketUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateTicketUseCase(repository: TicketRepository): CreateTicketUseCase {
        return CreateTicketUseCase(repository)
    }
}