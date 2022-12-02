package com.sample.shoppinglist.utils

import com.sample.shoppinglist.R

class FakeErrorString : UIString {

    override fun message(stringResID: Int) =
        Throwable(getMessage(stringResID))

    override fun message(stringResID: Int, vararg args: Any) =
        Throwable(getMessage(stringResID))

    private fun getMessage(stringResID: Int) = when (stringResID) {
        R.string.empty_filed_error -> "The fields must not be empty"
        R.string.max_name_error -> "The name of the item must not exceed $Constants characters"
        R.string.max_price_error -> "The price of the item must not exceed %s characters"
        R.string.valid_amount_msg -> "Please enter a valid amount"
        else -> "Unexpected error"
    }

}