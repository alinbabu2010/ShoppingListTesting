package com.sample.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.sample.shoppinglist.R
import com.sample.shoppinglist.data.local.ShoppingItem
import com.sample.shoppinglist.data.repositories.FakeShoppingRepository
import com.sample.shoppinglist.ui.fragments.AddShoppingItemFragment
import com.sample.shoppinglist.ui.fragments.AddShoppingItemFragmentDirections
import com.sample.shoppinglist.ui.fragments.ShoppingFragmentFactory
import com.sample.shoppinglist.ui.viewModel.ShoppingViewModel
import com.sample.shoppinglist.utils.FakeErrorString
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
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

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
        val testViewModel = ShoppingViewModel(FakeShoppingRepository(), FakeErrorString())
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }
        pressBack()
        assertThat(testViewModel.currentImageUrl.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun clickInsertIntoDbShoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepository(), FakeErrorString())
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Name"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())
        assertThat(testViewModel.shoppingItems.getOrAwaitValue()).contains(
            ShoppingItem(name = "Name", amount = 5, price = 5.5F, imageUrl = "")
        )
    }

    private fun mockNavControllerWithFragment(): NavController {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        return navController
    }

}