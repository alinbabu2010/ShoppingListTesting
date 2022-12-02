package com.sample.shoppinglist.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.sample.shoppinglist.ui.adapters.ImageAdapter
import com.sample.shoppinglist.ui.adapters.ShoppingItemAdapter
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }

}