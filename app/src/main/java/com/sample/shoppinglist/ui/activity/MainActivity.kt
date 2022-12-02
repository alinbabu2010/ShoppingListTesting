package com.sample.shoppinglist.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.shoppinglist.databinding.ActivityMainBinding
import com.sample.shoppinglist.ui.fragments.ShoppingFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
