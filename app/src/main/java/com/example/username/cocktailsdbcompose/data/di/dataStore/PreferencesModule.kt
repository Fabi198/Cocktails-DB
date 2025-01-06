package com.example.username.cocktailsdbcompose.data.di.dataStore

import android.content.Context
import com.example.username.cocktailsdbcompose.data.di.FirebaseAuthSingleton
import com.example.username.cocktailsdbcompose.data.di.FirebaseDBSingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext app: Context, firebaseAuthSingleton: FirebaseAuthSingleton, firebaseDBSingleton: FirebaseDBSingleton): PreferenceMethods = Preferences(app, firebaseAuthSingleton, firebaseDBSingleton)
}