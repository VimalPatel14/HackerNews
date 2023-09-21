package com.vimal.hackernews.repository

import com.vimal.hackernews.model.Id
import com.vimal.hackernews.network.HackerNewsService
import com.vimal.hackernews.network.Item
import com.vimal.hackernews.network.NetworkState
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
/**
 *  Repository for fetching hacker news data
 */
class HackerNewsRepository @Inject constructor(private val retrofitService: HackerNewsService) {

    suspend fun getAllCharacter(itemId: Int): NetworkState<Item> {
        val response = retrofitService.getAllCharacter(itemId)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null)
                NetworkState.Success(responseBody)
            else
                NetworkState.Error(response)
        } else
            NetworkState.Error(response)
    }

    suspend fun getAllIds(): Flow<Response<Id>> = flow {
        val response = retrofitService.getAllId()
        emit(response)
    }

    suspend fun getItem(itemId: Int): Flow<Response<Item>> = flow {
        val response = retrofitService.getAllCharacter(itemId)
        emit(response)
    }

    suspend fun getTopStories(): Response<Id> {
        val response = retrofitService.getAllId()
        return response
    }

}
