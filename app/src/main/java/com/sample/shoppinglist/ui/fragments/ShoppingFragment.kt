package com.sample.shoppinglist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sample.shoppinglist.R
import com.sample.shoppinglist.databinding.FragmentShoppingBinding
import com.sample.shoppinglist.ui.adapters.ShoppingItemAdapter
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment @Inject constructor(
    private val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment() {

    private lateinit var binding: FragmentShoppingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]

        subscribeToObservers()
        setupRecyclerView()

        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }

    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.currentList[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(
                requireView(),
                R.string.delete_success_msg,
                Snackbar.LENGTH_LONG
            ).apply {
                setAction(R.string.undo) {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }

    }

    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }

    private fun subscribeToObservers() {

        viewModel?.shoppingItems?.observe(viewLifecycleOwner) {
            shoppingItemAdapter.submitList(it)
        }

        viewModel?.totalPrice?.observe(viewLifecycleOwner) {
            val price = it ?: 0F
            val priceText = "Total price: ₹$price"
            binding.tvShoppingItemPrice.text = priceText
        }

    }

}