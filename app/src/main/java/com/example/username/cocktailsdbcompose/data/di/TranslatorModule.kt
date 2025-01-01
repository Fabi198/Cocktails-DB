package com.example.username.cocktailsdbcompose.data.di

import android.util.Log
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslatorModule {

    @Singleton
    @Provides
    fun provideTranslatorFactory(conditions: DownloadConditions): TranslatorFactory =
        TranslatorFactory(conditions)

    @Singleton
    @Provides
    fun provideConditions(): DownloadConditions =
        DownloadConditions.Builder().requireWifi().build()
}

class TranslatorFactory(private val conditions: DownloadConditions) {

    fun createTranslator(target: String): Translator {
        val options = TranslatorOptions
            .Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(target)
            .build()

        val translator = Translation.getClient(options)
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.i("Translator", "Model downloaded successfully")
            }
            .addOnFailureListener {
                Log.e("Translator", "Model download failed: ${it.message}")
            }
        return translator
    }

    fun ensureModelDownloaded(
        translator: Translator,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.i("Translator", "Model downloaded successfully")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("Translator", "Model download failed: ${exception.message}")
                onError(exception)
            }
    }

}