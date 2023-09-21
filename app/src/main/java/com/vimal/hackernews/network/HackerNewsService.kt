package com.vimal.hackernews.network

import com.vimal.hackernews.model.Id
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.Serializable

val BASE_URL = "https://hacker-news.firebaseio.com/"

/**
 * Retrofit service for the Hacker News API
 *
 * https://github.com/HackerNews/API
 */

// https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty
// https://hacker-news.firebaseio.com/v0/item/26068032.json?print=pretty
interface HackerNewsService{

    @GET("item/{itemId}.json?print=pretty")
    suspend fun getAllCharacter(@Path("itemId") itemId: Int): Response<Item>

    @GET("topstories.json?print=pretty")
    suspend fun getAllId(): Response<Id>
}

data class Item(
    val id: Int,
    val title: String,
    val text: String?,
    val time: Int,
    val type: String,
    val by: String?,
    val descendants: Int?,
    val kids: List<Int>?,
    val score: Int?,
    val url: String?,
) : Serializable
