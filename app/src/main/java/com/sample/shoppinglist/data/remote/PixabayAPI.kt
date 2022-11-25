package com.sample.shoppinglist.data.remote

import com.sample.shoppinglist.BuildConfig.API_KEY
import com.sample.shoppinglist.data.models.ImageResponse
import com.sample.shoppinglist.utils.Constants.API_ENDPOINT
import com.sample.shoppinglist.utils.Constants.PARAM_KEY
import com.sample.shoppinglist.utils.Constants.PARAM_QUERY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET(API_ENDPOINT)
    suspend fun searchForImage(
        @Query(PARAM_QUERY) searchQuery: String,
        @Query(PARAM_KEY) apiKey: String = API_KEY
    ): Response<ImageResponse>

}