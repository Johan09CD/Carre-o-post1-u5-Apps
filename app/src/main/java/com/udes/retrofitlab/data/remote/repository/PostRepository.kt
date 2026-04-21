package com.udes.retrofitlab.data.repository

import com.udes.retrofitlab.data.remote.api.PostApi
import com.udes.retrofitlab.data.remote.dto.toDomain
import com.udes.retrofitlab.domain.model.Post

class PostRepository(private val api: PostApi) {

    suspend fun getPosts(page: Int): Result<List<Post>> =
        runCatching {
            api.getPosts(page = page).map { it.toDomain() }
        }
}