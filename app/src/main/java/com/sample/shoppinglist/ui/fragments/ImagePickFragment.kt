package com.sample.shoppinglist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sample.shoppinglist.R
import com.sample.shoppinglist.data.models.Resource
import com.sample.shoppinglist.databinding.FragmentImagePickBinding
import com.sample.shoppinglist.ui.adapters.ImageAdapter
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import com.sample.shoppinglist.utils.Constants.GRID_SPAN_COUNT
import com.sample.shoppinglist.utils.Constants.SEARCH_TIME_DELAY
import com.sample.shoppinglist.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
) : Fragment() {

    private lateinit var binding: FragmentImagePickBinding
    val viewModel by viewModels<ShoppingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }


        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }

    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Resource.Status.SUCCESS -> {
                        val urls =
                            result.data?.imageResults?.map { imageResult -> imageResult.previewURL }
                        imageAdapter.submitList(urls ?: listOf())
                        binding.progressBar.isVisible = false
                    }
                    Resource.Status.ERROR -> {
                        showSnackBar(result.message ?: R.string.unknown_error)
                        binding.progressBar.isVisible = false
                    }
                    Resource.Status.LOADING -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> {

                        binding.progressBar.isVisible = false
                    }
                }
            }
        }
    }

}