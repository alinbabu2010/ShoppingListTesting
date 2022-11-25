package com.sample.shoppinglist.data.repositories

import androidx.lifecycle.LiveData
import com.sample.shoppinglist.data.local.ShoppingItem
import com.sample.shoppinglist.data.models.ImageResponse
import com.sample.shoppinglist.data.models.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>

}