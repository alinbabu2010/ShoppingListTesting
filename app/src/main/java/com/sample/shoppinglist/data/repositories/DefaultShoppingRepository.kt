package com.sample.shoppinglist.data.repositories

import androidx.lifecycle.LiveData
import com.sample.shoppinglist.data.local.ShoppingDao
import com.sample.shoppinglist.data.local.ShoppingItem
import com.sample.shoppinglist.data.models.ImageResponse
import com.sample.shoppinglist.data.models.Resource
import com.sample.shoppinglist.data.remote.PixabayAPI
import com.sample.shoppinglist.utils.Constants.UNKNOWN_ERROR
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.empty()
            } else Resource.error(Throwable(UNKNOWN_ERROR))
        } catch (exception: Exception) {
            Resource.error(exception)
        }
    }

}