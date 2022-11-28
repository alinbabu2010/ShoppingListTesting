package com.sample.shoppinglist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.shoppinglist.R
import com.sample.shoppinglist.data.local.ShoppingItem
import com.sample.shoppinglist.data.models.ImageResponse
import com.sample.shoppinglist.data.models.Resource
import com.sample.shoppinglist.data.repositories.ShoppingRepository
import com.sample.shoppinglist.utils.Constants
import com.sample.shoppinglist.utils.Event
import com.sample.shoppinglist.utils.UIString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository,
    private val errorString: UIString
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error(errorString.message(R.string.empty_filed_error)))
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        errorString.message(
                            R.string.max_name_error,
                            Constants.MAX_NAME_LENGTH
                        )
                    )
                )
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        errorString.message(
                            R.string.max_price_error,
                            Constants.MAX_PRICE_LENGTH
                        )
                    )
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (exception: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error(errorString.message(R.string.valid_amount_msg)))
            )
            return
        }
        val shoppingItem = ShoppingItem(
            name = name, amount = amount,
            price = priceString.toFloat(),
            imageUrl = _currentImageUrl.value ?: ""
        )
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }

}