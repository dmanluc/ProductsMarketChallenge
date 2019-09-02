package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.dmanluc.cabifymarket.data.remote.utils.MockDataProvider
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.data.remote.utils.getOrAwaitValue
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import com.dmanluc.cabifymarket.presentation.navigation.NavigationCommand
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.Event
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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
    private val dispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    @Before
    fun setUp() {
        marketProductsInteractor = mockk()
        saveProductsCartInteractor = mockk()
    }

    @Test
    fun `market products requested when ViewModel is created and no error found`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val result = Resource.success(MockDataProvider.createMockProductList())
        coEvery {
            marketProductsInteractor.invoke(false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = result
        }

        marketProductsViewModel = MarketProductsViewModel(marketProductsInteractor,
                                                          saveProductsCartInteractor, dispatchers)
        marketProductsViewModel.products.observeForever(observer)

        verify {
            observer.onChanged(result)
        }

        confirmVerified(observer)
    }

    @Test
    fun `market products requested but failed when ViewModel is created`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val observerSnackbar = mockk<Observer<Event<Int>>>(relaxed = true)
        val result = Resource.error(Exception("fail"), null)
        coEvery {
            marketProductsInteractor(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = result
        }

        marketProductsViewModel = MarketProductsViewModel(marketProductsInteractor,
                                                          saveProductsCartInteractor,
                                                          dispatchers).apply {
            products.observeForever(observer)
            snackBarError.observeForever(observerSnackbar)
        }

        verify {
            observer.onChanged(result)
            observerSnackbar.onChanged(marketProductsViewModel.snackBarError.value)
        }

        confirmVerified(observer)
    }

    @Test
    fun `add product to user´s products cart`() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)
        val mockProductList = MockDataProvider.createMockProductList()
        coEvery {
            marketProductsInteractor(false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = Resource.success(mockProductList)
        }
        coEvery { saveProductsCartInteractor.invoke(any()) } just Runs

        marketProductsViewModel = MarketProductsViewModel(marketProductsInteractor,
                                                          saveProductsCartInteractor,
                                                          dispatchers).apply {
            productsCart.observeForever(observer)
            addProductToCart(1, mockProductList.first())
        }

        verify {
            observer.onChanged(marketProductsViewModel.productsCart.value)
        }

        coVerify(exactly = 1) {
            saveProductsCartInteractor.invoke(marketProductsViewModel.productsCart.value!!)
        }

        confirmVerified(observer)
    }

    @Test
    fun `navigate to checkout screen`() {
        val mockProductList = MockDataProvider.createMockProductList()
        val event = Event(
            NavigationCommand.To(
                MarketProductsFragmentDirections.actionGoToCheckout(ProductsCart().apply {
                    addProduct(
                        1, mockProductList.first()
                    )
                })
            )
        )

        coEvery {
            marketProductsInteractor(false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = Resource.success(mockProductList)
        }
        coEvery { saveProductsCartInteractor.invoke(any()) } just Runs

        marketProductsViewModel = MarketProductsViewModel(marketProductsInteractor,
                                                          saveProductsCartInteractor, dispatchers)
        with(marketProductsViewModel) {
            addProductToCart(1, mockProductList.first())
            goToCheckout()
        }

        coVerify(exactly = 1) {
            saveProductsCartInteractor.invoke(marketProductsViewModel.productsCart.value!!)
        }

        Assert.assertEquals(
            event.peekContent(), marketProductsViewModel.navigation.getOrAwaitValue().peekContent()
        )
    }

    @Test
    fun `user refreshes list with swipe to refresh`() {
        val observer = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val mockProductList = MockDataProvider.createMockProductList()
        coEvery {
            marketProductsInteractor(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = Resource.success(mockProductList)
        }

        marketProductsViewModel = MarketProductsViewModel(marketProductsInteractor,
                                                          saveProductsCartInteractor,
                                                          dispatchers).apply {
            products.observeForever(observer)
            refreshMarketProducts()
        }

        verify {
            observer.onChanged(Resource.success(mockProductList))
            observer.onChanged(Resource.success(mockProductList))
        }

        confirmVerified(observer)
    }

}