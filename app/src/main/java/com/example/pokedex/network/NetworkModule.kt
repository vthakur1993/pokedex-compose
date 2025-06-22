package com.example.pokedex.network

import android.R.attr.level
import com.example.pokedex.network.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().run {
            baseUrl(BASE_URL)
            addConverterFactory(MoshiConverterFactory.create())
            client(client)
            this.build()
        }
    }

    @Singleton
    @Provides
    fun providePokedexApiService(retrofit: Retrofit): PokedexApiService {
        return retrofit.create(PokedexApiService::class.java)
    }
}