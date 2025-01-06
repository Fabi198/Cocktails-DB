package com.example.username.cocktailsdbcompose.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    @Provides
    fun provideFirebaseDatabase(): FirebaseDBSingleton {
        return FirebaseDBSingleton()
    }

}

@Singleton
class FirebaseAuthSingleton @Inject constructor() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
}

@Singleton
class FirebaseDBSingleton @Inject constructor() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
}

object DBNames {
    val USERS = "users"
    val RECENT_COCKTAILS = "recentCocktails"
    val SAVED_COCKTAILS = "savedCocktails"
    val FAVORITES_COCKTAILS = "favoritesCocktails"
}