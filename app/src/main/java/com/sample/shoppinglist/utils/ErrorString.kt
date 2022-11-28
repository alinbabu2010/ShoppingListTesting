package com.sample.shoppinglist.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorString @Inject constructor() : UIString {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun message(stringResID: Int) =
        Throwable(context.getString(stringResID))


    override fun message(stringResID: Int, vararg args: Any) =
        Throwable(context.getString(stringResID, args))


}