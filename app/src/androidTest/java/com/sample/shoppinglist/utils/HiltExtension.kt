package com.sample.shoppinglist.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.sample.shoppinglist.HiltTestActivity
import com.sample.shoppinglist.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {

    val bundleKey =
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY"

    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(bundleKey, themeResId)

    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->

        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }

        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )

        fragment.arguments = fragmentArgs

        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        (fragment as T).action()

    }

}