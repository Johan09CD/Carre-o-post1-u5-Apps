package com.udes.retrofitlab.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udes.retrofitlab.data.repository.PostRepository
import com.udes.retrofitlab.di.NetworkModule
import com.udes.retrofitlab.domain.error.toAppError
import com.udes.retrofitlab.domain.error.toMessage
import com.udes.retrofitlab.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    private val repo = PostRepository(NetworkModule.postApi)

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private val allPosts = mutableListOf<Post>()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = PostsUiState.Loading
            repo.getPosts(currentPage)
                .onSuccess { posts ->
                    allPosts.addAll(posts)
                    _uiState.value = if (allPosts.isEmpty())
                        PostsUiState.Empty
                    else
                        PostsUiState.Success(allPosts.toList())
                }
                .onFailure { error ->
                    _uiState.value = PostsUiState.Error(
                        error.toAppError().toMessage()
                    )
                }
        }
    }

    fun loadNextPage() {
        currentPage++
        viewModelScope.launch {
            repo.getPosts(currentPage)
                .onSuccess { newPosts ->
                    allPosts.addAll(newPosts)
                    _uiState.value = PostsUiState.Success(allPosts.toList())
                }
                .onFailure { error ->
                    _uiState.value = PostsUiState.Error(
                        error.toAppError().toMessage()
                    )
                }
        }
    }
}