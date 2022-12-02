package com.sample.shoppinglist.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.sample.shoppinglist.data.repositories.FakeShoppingRepository
import com.sample.shoppinglist.ui.adapters.ImageAdapter
import com.sample.shoppinglist.ui.adapters.ShoppingItemAdapter
import com.sample.shoppinglist.ui.fragments.AddShoppingItemFragment
import com.sample.shoppinglist.ui.fragments.ImagePickFragment
import com.sample.shoppinglist.ui.fragments.ShoppingFragment
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import com.sample.shoppinglist.utils.FakeErrorString
import javax.inject.Inject

class TestShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter,
    private val glide: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter,
                ShoppingViewModel(FakeShoppingRepository(), FakeErrorString())
            )
            else -> super.instantiate(classLoader, className)
        }
    }

}