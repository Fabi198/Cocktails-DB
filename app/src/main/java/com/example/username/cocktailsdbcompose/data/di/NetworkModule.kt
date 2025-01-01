package com.example.username.cocktailsdbcompose.data.di

import com.example.username.cocktailsdbcompose.data.CocktailsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): CocktailsApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(CocktailsApiService::class.java)

    @Provides
    fun provideOkHTTPClient(): OkHttpClient =
        OkHttpClient.Builder().build()
}