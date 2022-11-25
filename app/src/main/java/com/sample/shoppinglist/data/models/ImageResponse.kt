package com.sample.shoppinglist.data.models

data class ImageResponse(
    val imageResults: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)