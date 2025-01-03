package com.example.username.cocktailsdbcompose.data.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuthSingleton(): FirebaseAuthSingleton {
        return FirebaseAuthSingleton()
    }

}

@Singleton
class FirebaseAuthSingleton @Inject constructor() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
}