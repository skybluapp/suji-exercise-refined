package com.suji.sujitechnicalexercise.dependencyInjection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.connectedSujiDevices.ConnectedSujiDevices
import com.connectedSujiDevices.ConnectedSujiDevicesInterface
import com.firestore.FireStoreRead
import com.firestore.ReadServerInterface
import com.google.firebase.FirebaseApp
import com.repository.Repository
import com.suji.domain.AthletesPaging
import com.suji.domain.AthletesPagingInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependancy Injection Module
 * Provides instances of interfaces used throughout the app
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideTestString(): String {
        return "This is a test string"
    }


    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }



    @Singleton
    @Provides
    fun provideRepository(
        readServerInterface: ReadServerInterface,
        connectedSujiDevicesInterface: ConnectedSujiDevicesInterface
    ): Repository {
        return Repository(
            readServerInterface = readServerInterface,
            connectedSujiDevicesInterface = connectedSujiDevicesInterface
        )
    }

    @Singleton
    @Provides
    fun provideFirestoreRead(): FireStoreRead {
        return FireStoreRead()
    }

    @Singleton
    @Provides
    fun provideConnectedSuji(): ConnectedSujiDevices {
        return ConnectedSujiDevices()
    }

    @Singleton
    @Provides
    fun provideAthletesPaging(repository: Repository): AthletesPaging {
        return AthletesPaging(repository = repository)
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
    abstract fun connectedSujiInterface(connectedSujiDevices: ConnectedSujiDevices): ConnectedSujiDevicesInterface

    @Singleton
    @Binds
    abstract fun athletesPaging(athletesPaging: AthletesPaging): AthletesPagingInterface
}
