package com.sample.shoppinglist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.shoppinglist.databinding.FragmentAddShoppingItemBinding

class AddShoppingItemFragment : Fragment() {

    private lateinit var binding: FragmentAddShoppingItemBinding
    private val viewModel by viewModels<ShoppingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShoppingItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}