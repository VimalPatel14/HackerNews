package com.vimal.hackernews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vimal.hackernews.model.Id
import com.vimal.hackernews.network.Item
import com.vimal.hackernews.network.NetworkState
import com.vimal.hackernews.repository.HackerNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * ViewModel for [com.kroger.android.interview.hackernews.MainActivity]
 */

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: HackerNewsRepository) :
    ViewModel() {

//    suspend fun getAllIds(): Flow<Response<Id>> {
//        return repository.getAllIds()
//    }
//
//    suspend fun getItem(itemId: Int): Flow<Response<Item>> {
//        return repository.getItem(itemId)
//    }


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val characterList = MutableLiveData<Item>()

    fun fetchTopStories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getTopStories()
                if (response.isSuccessful) {
                    val list = response.body()
                    if (list != null) {
                        list.forEach { id ->
                            getAllCharacter(id)
                        }
                    } else {
                        onError("Response body is null")
                    }
                } else {
                    onError("Request failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Exception handled: ${e.localizedMessage}")
            }
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun getAllCharacter(itemId: Int) {
        viewModelScope.launch(exceptionHandler) {
            when (val response = repository.getAllCharacter(itemId)) {
                is NetworkState.Success -> characterList.postValue(response.data!!)
                is NetworkState.Error -> onError("Network Error")
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }
}
