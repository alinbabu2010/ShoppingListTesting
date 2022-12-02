package com.sample.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.sample.shoppinglist.R
import com.sample.shoppinglist.ui.fragments.AddShoppingItemFragment
import com.sample.shoppinglist.ui.fragments.AddShoppingItemFragmentDirections
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import com.sample.shoppinglist.utils.getOrAwaitValue
import com.sample.shoppinglist.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun pressBackButtonPopBackStack() {
        val navController = mockNavControllerWithFragment()
        pressBack()
        verify(navController).popBackStack()
    }

    @Test
    fun clickShoppingImageViewNavigateToImagePickFragment() {
        val navController = mockNavControllerWithFragment()
        onView(withId(R.id.ivShoppingImage)).perform(click())
        verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }

    @Test
    fun checkViewModelCurImageUrlIsEmptyAfterBackPress() {
        var shoppingViewModel: ShoppingViewModel? = null
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            shoppingViewModel = viewModel
        }
        pressBack()
        assertThat(shoppingViewModel?.currentImageUrl?.getOrAwaitValue()).isEmpty()
    }

    private fun mockNavControllerWithFragment(): NavController {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        return navController
    }

}