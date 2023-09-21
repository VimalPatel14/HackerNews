package com.vimal.hackernews.network

import com.vimal.hackernews.repository.HackerNewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://hacker-news.firebaseio.com/v0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesAPI(retrofit: Retrofit): HackerNewsService {
        return retrofit.create(HackerNewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideHackerNewsRepository(service: HackerNewsService): HackerNewsRepository {
        return HackerNewsRepository(service)
    }
}