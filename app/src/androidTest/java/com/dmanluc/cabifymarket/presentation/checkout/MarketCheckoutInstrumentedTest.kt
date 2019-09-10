package com.dmanluc.cabifymarket.presentation.checkout

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.di.marketCheckoutModule
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.espressoRecyclerViewActions.RecyclerViewHolderItemViewAction
import com.dmanluc.cabifymarket.espressoRecyclerViewActions.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.dmanluc.cabifymarket.presentation.feature.checkout.MarketCheckoutAdapter
import com.dmanluc.cabifymarket.presentation.feature.checkout.MarketCheckoutFragment
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.utils.executePendingDataBindingTransactions
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * https://stackoverflow.com/questions/40703567/how-do-i-make-espresso-wait-until-data-binding-has-updated-the-view-with-the-dat/52390905#52390905
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MarketCheckoutInstrumentedTest : AutoCloseKoinTest() {

    private val mockProductsCart = MockDataProvider.createMockProductsCart()

    private var localProductsCartRepository = mockk<ProductsCartLocalRepository>()
    private var componentIdlingResource: CountingIdlingResource? = null

    private lateinit var fragment: MarketCheckoutFragment

    @Before
    fun setUp() {
        startKoin {
            modules(listOf(module {
                factory { localProductsCartRepository }
            }, marketCheckoutModule))
        }

        launchFragment()
    }

    @After
    fun tearDown() {
        componentIdlingResource?.let { IdlingRegistry.getInstance().unregister(it) }
    }

    @Test
    fun loadProductsCart_checkoutListContainsItsItems() {
        onView(withId(R.id.cartProductsRecycler)).check(withItemCount(mockProductsCart.getProducts().size))

        onView(withId(R.id.checkoutOrderInfoTotalPrice)).check(
            matches(
                withText(
                    CurrencyAmount(mockProductsCart.getTotalAmount()).formatCurrencyInLocale()
                )
            )
        )

        fragment.executePendingDataBindingTransactions()

        onView(withId(R.id.cartPayment)).check(matches(isEnabled()))
    }

    @Test
    fun updateProductItemQuantity_shouldUpdateTotalPrice() {
        onView(withId(R.id.cartProductsRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MarketCheckoutAdapter.ProductViewHolder>(
                    0,
                    RecyclerViewHolderItemViewAction.withChildItemId(R.id.increment)
                )
            )

        fragment.executePendingDataBindingTransactions()

        onView(withId(R.id.checkoutOrderInfoTotalPrice)).check(
            matches(
                withText(
                    CurrencyAmount(mockProductsCart.getTotalAmount()).formatCurrencyInLocale()
                )
            )
        )

        onView(withId(R.id.cartPayment)).check(matches(isEnabled()))
    }

    @Test
    fun removeAllProductsFromCart_shouldDisablePaymentButton() {
        repeat(mockProductsCart.getProducts().size) {
            onView(withId(R.id.cartProductsRecycler))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<MarketCheckoutAdapter.ProductViewHolder>(
                        0,
                        RecyclerViewHolderItemViewAction.withChildItemId(R.id.removeProductFromCart)
                    )
                )
        }

        fragment.executePendingDataBindingTransactions()

        onView(withId(R.id.checkoutOrderInfoTotalPrice)).check(
            matches(
                withText(
                    CurrencyAmount(mockProductsCart.getTotalAmount()).formatCurrencyInLocale()
                )
            )
        )

        onView(withId(R.id.emptyProductsCartTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.cartPayment)).check(matches(not(isEnabled())))
    }

    @Test
    fun completePayment_shouldFinishCheckoutFlow() {
        coEvery { localProductsCartRepository.deleteProductsCart(any()) } just Runs

        onView(withId(R.id.checkoutOrderInfo)).perform(swipeUp())
        onView(withId(R.id.cartPayment)).perform(click())

        onView(isRoot()).perform(pressBackUnconditionally())
    }

    private fun launchFragment(): NavController {
        val mockNavController = mockk<NavController>(relaxed = true)
        val marketProductsScenario =
            launchFragmentInContainer<MarketCheckoutFragment>(fragmentArgs = Bundle().apply {
                putSerializable("productsCart", mockProductsCart)
            }, themeResId = R.style.AppTheme)

        marketProductsScenario.onFragment { fragment ->
            this.fragment = fragment

            componentIdlingResource = fragment.countingIdlingResource
            IdlingRegistry.getInstance().register(componentIdlingResource)

            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        return mockNavController
    }

}
