package com.suji.sujitechnicalexercise.dependencyInjection

import com.firestore.FireStoreRead
import com.firestore.ReadServerInterface
import com.repository.Repository
import com.suji.domain.AthletesPaging
import com.suji.domain.AthletesPagingInterface
import com.suji.domain.connectedSujiDevices.ConnectedSujiDevicesBimap
import com.suji.domain.connectedSujiDevices.SujiDeviceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides instances of interfaces used throughout the app
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        readServerInterface: ReadServerInterface,
    ): Repository {
        return Repository(
            readServerInterface = readServerInterface,
        )
    }

    @Singleton
    @Provides
    fun provideFirestoreRead(): FireStoreRead {
        return FireStoreRead()
    }

    @Singleton
    @Provides
    fun provideAthletesPaging(repository: Repository, connectedSujiDevices: ConnectedSujiDevicesBimap): AthletesPaging {
        return AthletesPaging(repository = repository, connectedSujiDevices = connectedSujiDevices)
    }

    @Singleton
    @Provides
    fun provideConnectedSujis() : ConnectedSujiDevicesBimap{
        return ConnectedSujiDevicesBimap()
    }
}

/**
 * Binds interfaces to specific implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindings {

    @Singleton
    @Binds
    abstract fun readServerInterface(fireStore: FireStoreRead): ReadServerInterface

    @Singleton
    @Binds
    abstract fun connectedSujiInterface(connectedSujiDevices: ConnectedSujiDevicesBimap) : SujiDeviceManager

    @Singleton
    @Binds
    abstract fun athletesPaging(athletesPaging: AthletesPaging): AthletesPagingInterface
}
