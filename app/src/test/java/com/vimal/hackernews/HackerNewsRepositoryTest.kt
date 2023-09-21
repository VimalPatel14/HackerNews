package com.vimal.hackernews

import com.vimal.hackernews.model.Id
import com.vimal.hackernews.network.HackerNewsService
import com.vimal.hackernews.network.Item
import com.vimal.hackernews.network.NetworkState
import com.vimal.hackernews.repository.HackerNewsRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class HackerNewsRepositoryTest {

    @Mock
    private lateinit var hackerNewsService: HackerNewsService

    private lateinit var hackerNewsRepository: HackerNewsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        hackerNewsRepository = HackerNewsRepository(hackerNewsService)
    }

    @Test
    fun testGetAllCharacterSuccess() = runBlocking {
        // Arrange
        val itemId = 1
        val item = Item(
            id = 1, title = "vimal", text = "", time = 1, type = "01", by = "",
            descendants = 0, kids = emptyList(), score = 0, url = ""
        )
        val response = Response.success(item)

        Mockito.`when`(hackerNewsService.getAllCharacter(itemId)).thenReturn(response)

        // Act
        val networkState = hackerNewsRepository.getAllCharacter(itemId)

        // Assert
        assertTrue(networkState is NetworkState.Success)
        assertEquals(item, (networkState as NetworkState.Success).data)
    }

    @Test
    fun testGetAllCharacterError() = runBlocking {
        // Arrange
        val itemId = 1
        val response = Response.error<Item>(404, ResponseBody.create(null, ""))

        Mockito.`when`(hackerNewsService.getAllCharacter(itemId)).thenReturn(response)

        // Act
        val networkState = hackerNewsRepository.getAllCharacter(itemId)

        // Assert
        assertTrue(networkState is NetworkState.Error)
    }

    @Test
    fun testGetTopStories() = runBlocking {
        // Arrange
        val id =Id()
        id.add(1)
        id.add(2)
        id.add(3)
        val response = Response.success(id)
        Mockito.`when`(hackerNewsService.getAllId()).thenReturn(response)

        // Act
        val result = hackerNewsRepository.getTopStories()

        // Assert
        assertTrue(result.isSuccessful) // Check if the response is successful
        assertEquals(id, result.body()) // Check if the response body matches the expected list
    }
}
