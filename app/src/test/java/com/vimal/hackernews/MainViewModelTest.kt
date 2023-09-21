package com.vimal.hackernews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vimal.hackernews.model.Id
import com.vimal.hackernews.network.Item
import com.vimal.hackernews.network.NetworkState
import com.vimal.hackernews.repository.HackerNewsRepository
import com.vimal.hackernews.viewmodel.MainViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: HackerNewsRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(repository)

        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun testFetchTopStoriesSuccess() = runBlocking {
        // Arrange
        val topStories = Id()
        topStories.add(1)
        topStories.add(2)
        topStories.add(3)
        val item = Item(
            id = 1, title = "vimal", text = "", time = 1, type = "01", by = "",
            descendants = 0, kids = emptyList(), score = 0, url = ""
        )

        val response = Response.success(topStories)

        Mockito.`when`(repository.getTopStories()).thenReturn(response)
        Mockito.`when`(repository.getAllCharacter(1)).thenReturn(NetworkState.Success(item))

        // Observer for characterList
        val characterListObserver = Observer<Item> {
            // Assert that characterList has the expected value
            assertEquals(item, it)
        }

        try {
            // Observe characterList
            viewModel.characterList.observeForever(characterListObserver)

            // Act
            viewModel.fetchTopStories()

        } finally {
            // Cleanup: Remove the observer
            viewModel.characterList.removeObserver(characterListObserver)
        }
    }

    @Test
    fun testFetchTopStoriesError() = runBlocking {
        val id =Id()
        id.add(1)
        id.add(2)
        id.add(3)
        val errorMessage = "Network Error"
        val responsetop = Response.success(id)
        Mockito.`when`(repository.getTopStories()).thenReturn(responsetop)
        val response: Response<Item> = Response.error(404, ResponseBody.create(null, ""))
        Mockito.`when`(repository.getAllCharacter(1)).thenReturn(NetworkState.Error(response))


        // Observer for errorMessage
        val errorMessageObserver = Observer<String> {
            // Assert that errorMessage has the expected value
            assertEquals(errorMessage, it)
        }

        try {
            // Observe errorMessage
            viewModel.errorMessage.observeForever(errorMessageObserver)

            // Act
            viewModel.fetchTopStories()

        } finally {
            // Cleanup: Remove the observer
            viewModel.errorMessage.removeObserver(errorMessageObserver)
        }
    }
}
