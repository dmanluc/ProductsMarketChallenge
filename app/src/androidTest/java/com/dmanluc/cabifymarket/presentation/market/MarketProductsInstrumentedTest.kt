package com.dmanluc.cabifymarket.presentation.market

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.di.marketProductsModule
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.MarketProductsLocalRepository
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.espressoRecyclerViewActions.RecyclerViewHolderItemViewAction.Companion.withChildItemId
import com.dmanluc.cabifymarket.espressoRecyclerViewActions.RecyclerViewItemCountAssertion.Companion.withItemCount
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsAdapter
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsFragment
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsFragmentDirections
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.utils.executePendingDataBindingTransactions
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.hamcrest.Matchers.not
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
 * To solve the problem waiting for data binding to get view updated with data model in Espresso UI tests:
 * @see: https://stackoverflow.com/questions/40703567/how-do-i-make-espresso-wait-until-data-binding-has-updated-the-view-with-the-dat/52390905#52390905
 *
 * Also, executePendingBindings on component data binding should be enough: the binding will be executed
immediately when calling that method, and not during the next frame of Choreographer
 *
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MarketProductsInstrumentedTest : AutoCloseKoinTest() {

    private val mockProducts = MockDataProvider.createMockProductList()
    private val mockProductsCart = MockDataProvider.createMockProductsCart()

    private var marketRepository = mockk<MarketRepository>()
    private var localProductsCartRepository = mockk<ProductsCartLocalRepository>()
    private lateinit var fragment: MarketProductsFragment

    @Before
    fun setUp() {
        startKoin {
            modules(listOf(module {
                factory { AppDispatchers(Dispatchers.Main, Dispatchers.Main) }
                factory { marketRepository }
                factory { localProductsCartRepository }
                factory { mockk<MarketProductsLocalRepository>() }
            }, marketProductsModule))
        }
    }

    @Test
    fun fetchProductsOnFragmentLaunch_whenSuccessFetchingData_shouldUpdateProductList() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.success(mockProducts)
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(mockProductsCart)
            )
        }

        launchFragment()

        onView(withId(R.id.productsRecycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )
        onView(withId(R.id.productsRecycler)).check(withItemCount(3))
    }

    @Test
    fun fetchProductsOnFragmentLaunch_whenErrorFetchingAndNoLocalProductsDataFound_shouldShowEmptyProductListInfo() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.error(
                    Exception("wtf"), listOf()
                )
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(ProductsCart())
            )
        }

        launchFragment()

        onView(withId(R.id.emptyProductsTextView))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.emptyProductsImageView))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(ViewMatchers.withText("wtf")))
        onView(withId(R.id.cartCheckout)).check(matches(not(isEnabled())))
    }

    @Test
    fun fetchProductsOnFragmentLaunch_whenNoInternetAndLocalProductsDataFound_shouldUpdateListWithLocalItems() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.error(
                    Exception("no_internet"), mockProducts
                )
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(mockProductsCart)
            )
        }

        launchFragment()

        onView(withId(R.id.productsSwipeRefreshLayout))
            .perform(ViewActions.swipeDown())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(ViewMatchers.withText("no_internet")))
        onView(withId(R.id.productsRecycler))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.productsRecycler)).check(withItemCount(3))
        onView(withId(R.id.cartCheckout)).check(matches(isEnabled()))
    }

    @Test
    fun clickOnCheckoutButton_shouldLaunchCheckoutScreen() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.success(mockProducts)
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(mockProductsCart)
            )
        }

        val mockNavController = launchFragment()

        onView(withId(R.id.cartCheckout)).perform(ViewActions.click())

        verify {
            mockNavController.navigate(
                MarketProductsFragmentDirections.actionGoToCheckout(
                    mockProductsCart
                ), any<FragmentNavigator.Extras>()
            )
        }
    }

    @Test
    fun addProductToShoppingCart_shouldEnableCheckoutButton() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.success(mockProducts)
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(mockProductsCart)
            )
        }

        coEvery { localProductsCartRepository.saveLocalProductsCart(any()) } just Runs

        launchFragment()

        onView(withId(R.id.productsRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MarketProductsAdapter.ProductViewHolder>(
                    0,
                    withChildItemId(R.id.addToCart)
                )
            )

        fragment.executePendingDataBindingTransactions()

        onView(withId(R.id.cartCheckout)).check(matches(isEnabled()))
    }

    @Test
    fun fetchProductsOnFragmentLaunch_whenLocalProductsCartFound_shouldShowCheckoutButtonEnabled() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.success(mockProducts)
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(mockProductsCart)
            )
        }

        coEvery { localProductsCartRepository.saveLocalProductsCart(any()) } just Runs

        launchFragment()

        onView(withId(R.id.cartCheckout)).check(matches(isEnabled()))
    }

    @Test
    fun fetchProductsOnFragmentLaunch_whenNoLocalProductsCartFound_shouldShowCheckoutButtonDisabled() {
        coEvery { marketRepository.getProducts(any()) } returns MutableLiveData<Resource<List<Product>>>().apply {
            postValue(
                Resource.success(mockProducts)
            )
        }

        coEvery { localProductsCartRepository.getLocalProductsCart() } returns MutableLiveData<Resource<ProductsCart>>().apply {
            postValue(
                Resource.success(ProductsCart())
            )
        }

        coEvery { localProductsCartRepository.saveLocalProductsCart(any()) } just Runs

        launchFragment()

        onView(withId(R.id.cartCheckout)).check(matches(not(isEnabled())))
    }

    private fun launchFragment(): NavController {
        val mockNavController = mockk<NavController>(relaxed = true)
        val marketProductsScenario =
            launchFragmentInContainer<MarketProductsFragment>(themeResId = R.style.AppTheme)
        marketProductsScenario.onFragment { fragment ->
            this.fragment = fragment
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        return mockNavController
    }

}
