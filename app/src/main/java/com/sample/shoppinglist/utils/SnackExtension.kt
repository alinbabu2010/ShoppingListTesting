package com.sample.shoppinglist.utils

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

inline fun <reified T> Fragment.showSnackBar(value: T) {

    val message = when (T::class) {
        Int::class -> getString(value as Int)
        else -> value as String
    }

    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()

}