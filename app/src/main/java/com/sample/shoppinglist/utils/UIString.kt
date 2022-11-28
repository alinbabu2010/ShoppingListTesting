package com.sample.shoppinglist.utils

interface UIString {
    fun message(stringResID: Int): Throwable
    fun message(stringResID: Int, vararg args: Any): Throwable
}