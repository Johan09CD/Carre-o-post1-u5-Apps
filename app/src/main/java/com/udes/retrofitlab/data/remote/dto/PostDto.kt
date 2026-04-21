package com.udes.retrofitlab.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.udes.retrofitlab.domain.model.Post

@Serializable
data class PostDto(
    val id: Int,
    @SerialName("userId") val userId: Int,
    val title: String,
    val body: String
)

fun PostDto.toDomain() = Post(
    id = id,
    userId = userId,
    title = title,
    excerpt = body.take(100).trimEnd() + if (body.length > 100) "…" else ""
)