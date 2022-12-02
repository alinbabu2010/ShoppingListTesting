package com.sample.shoppinglist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.sample.shoppinglist.R
import com.sample.shoppinglist.data.models.Resource.Status.*
import com.sample.shoppinglist.databinding.FragmentAddShoppingItemBinding
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment() {

    private lateinit var binding: FragmentAddShoppingItemBinding
    lateinit var viewModel: ShoppingViewModel

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.setCurImageUrl("")
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShoppingItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        subscribeToObservers()

        binding.run {

            btnAddShoppingItem.setOnClickListener {
                viewModel.insertShoppingItem(
                    etShoppingItemName.text.toString(),
                    etShoppingItemAmount.text.toString(),
                    etShoppingItemPrice.text.toString()
                )
            }

            ivShoppingImage.setOnClickListener {
                findNavController().navigate(
                    AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
                )
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private fun subscribeToObservers() {

        viewModel.currentImageUrl.observe(viewLifecycleOwner) {
            glide.load(it).into(binding.ivShoppingImage)
        }

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    SUCCESS -> showSnackBar(R.string.added_item)
                    ERROR -> showSnackBar(result.message ?: R.string.unknown_error)
                    LOADING -> { /* no-op */
                    }
                    EMPTY_RESPONSE -> showSnackBar(R.string.no_images)
                }
            }
        }

    }

    private inline fun <reified T> showSnackBar(value: T) {
        val message = when (T::class) {
            Int::class -> getString(value as Int)
            else -> value as String
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

}