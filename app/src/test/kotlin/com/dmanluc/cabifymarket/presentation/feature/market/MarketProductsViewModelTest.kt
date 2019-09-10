package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.data.remote.utils.getOrAwaitValue
import com.dmanluc.cabifymarket.data.remote.utils.observeForTesting
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.interactor.GetLastSavedProductsCartInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import com.dmanluc.cabifymarket.presentation.navigation.NavigationCommand
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.Event
import com.dmanluc.cabifymarket.utils.MockDataProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Daniel Manrique Lucas <dmanluc91></dmanluc91>@gmail.com>
 * @version 1
 * @since 2019-09-02.
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class MarketProductsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var marketProductsInteractor: GetProductsInteractor
    private lateinit var saveProductsCartInteractor: SaveProductsCartInteractor
    private lateinit var marketProductsViewModel: MarketProductsViewModel
    private lateinit var lastSavedProductsCartInteractor: GetLastSavedProductsCartInteractor
    private val dispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    private val mockProductListResource = Resource.success(MockDataProvider.createMockProductList())
    private val mockProductCartResource = Resource.success(ProductsCart())

    @Before
    fun setUp() {
        marketProductsInteractor = mockk()
        saveProductsCartInteractor = mockk()
        lastSavedProductsCartInteractor = mockk()

        coEvery {
            marketProductsInteractor.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListResource
        }

        coEvery { saveProductsCartInteractor.invoke(any()) } just Runs

        coEvery {
            lastSavedProductsCartInteractor.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockProductCartResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsInteractor,
            saveProductsCartInteractor,
            lastSavedProductsCartInteractor,
            dispatchers
        )
    }

    @Test
    fun `market products requested when ViewModel is created and no error found`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)

        marketProductsViewModel.products.observeForTesting(observer) {
            verifyAll {
                observer.onChanged(mockProductListResource)
            }

            confirmVerified(observer)
        }
    }

    @Test
    fun `market products requested but failed when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val observerSnackbar = mockk<Observer<Event<String>>>(relaxed = true)

        val result = Resource.error(Exception("fail"), null)
        coEvery {
            marketProductsInteractor(forceRefresh = false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = result
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsInteractor,
            saveProductsCartInteractor,
            lastSavedProductsCartInteractor,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.observeForever(observer)
            snackbarErrorWithStringLiteral.observeForever(observerSnackbar)
        }

        verify {
            observer.onChanged(result)
            observerSnackbar.onChanged(any())
        }

        confirmVerified(observer)
    }

    @Test
    fun `add product to user´s products cart`() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)
        val mockProductList = MockDataProvider.createMockProductList()

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLastSavedProductsCart(mockProductListResource)
            }
            productsCart.observeForTesting(observer) {
                addProductToCart(1, mockProductList.first())

                val cartSlot = slot<ProductsCart>()

                verify {
                    observer.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    saveProductsCartInteractor.invoke(any())
                    lastSavedProductsCartInteractor.invoke()
                }

                confirmVerified(observer)

                Assert.assertEquals(cartSlot.captured.size(), 1)
            }
        }
    }

    @Test
    fun `navigate to checkout screen`() {
        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLastSavedProductsCart(mockProductListResource)
            }
        }

        val event = Event(
            NavigationCommand.To(
                MarketProductsFragmentDirections.actionGoToCheckout(ProductsCart())
            )
        )

        marketProductsViewModel.goToCheckout()

        Assert.assertEquals(
            event.peekContent(), marketProductsViewModel.navigation.getOrAwaitValue().peekContent()
        )
    }

    @Test
    fun `user refreshes list with swipe to refresh`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)

        marketProductsViewModel.products.observeForTesting(observer) {
            marketProductsViewModel.refreshMarketProducts()

            verify {
                observer.onChanged(mockProductListResource)
                observer.onChanged(mockProductListResource)
            }

            confirmVerified(observer)
        }
    }

}