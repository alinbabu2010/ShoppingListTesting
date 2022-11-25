package com.sample.shoppinglist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.shoppinglist.databinding.FragmentShoppingBinding

class ShoppingFragment : Fragment() {

    private lateinit var binding: FragmentShoppingBinding
    private val viewModel by viewModels<ShoppingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}