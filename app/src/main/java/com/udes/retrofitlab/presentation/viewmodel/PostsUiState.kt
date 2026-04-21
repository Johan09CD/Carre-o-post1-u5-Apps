package com.udes.retrofitlab.presentation.viewmodel

import com.udes.retrofitlab.domain.model.Post

sealed class PostsUiState {
    object Loading : PostsUiState()
    object Empty : PostsUiState()
    data class Success(val posts: List<Post>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}