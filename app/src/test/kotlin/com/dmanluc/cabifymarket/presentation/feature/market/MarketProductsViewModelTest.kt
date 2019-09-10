package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.dmanluc.cabifymarket.data.remote.utils.getOrAwaitValue
import com.dmanluc.cabifymarket.data.remote.utils.observeForTesting
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.usecase.GetLocalProductsCartUseCase
import com.dmanluc.cabifymarket.domain.usecase.GetMarketProductsUseCase
import com.dmanluc.cabifymarket.domain.usecase.SaveLocalProductsCartUseCase
import com.dmanluc.cabifymarket.presentation.navigation.NavigationCommand
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.Event
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.utils.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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

    private lateinit var marketProductsUseCase: GetMarketProductsUseCase
    private lateinit var saveLocalProductsCartUseCase: SaveLocalProductsCartUseCase
    private lateinit var marketProductsViewModel: MarketProductsViewModel
    private lateinit var localProductsCartUseCase: GetLocalProductsCartUseCase
    private val dispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    private val mockProductListSuccessResource =
        Resource.success(MockDataProvider.createMockProductList())
    private val mockProductListErrorResourceWithNoLocalData =
        Resource.error(Exception("fail"), null)
    private val mockProductListErrorResourceWithLocalData =
        Resource.error(Exception("fail"), MockDataProvider.createMockProductList())
    private val mockEmptyProductCartSuccessResource = Resource.success(ProductsCart())
    private val mockNotEmptyProductCartSuccessResource =
        Resource.success(MockDataProvider.createMockProductsCart())

    @Before
    fun setUp() {
        marketProductsUseCase = mockk()
        saveLocalProductsCartUseCase = mockk()
        localProductsCartUseCase = mockk()
    }

    @Test
    fun `market products requested with no error found when ViewModel is created and no local product cart found`() {
        val productsObserver = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val productsCartObserver = mockk<Observer<ProductsCart>>(relaxed = true)

        coEvery {
            marketProductsUseCase.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListSuccessResource
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListSuccessResource)
            }

            products.observeForTesting(productsObserver) {
                verify {
                    productsObserver.onChanged(mockProductListSuccessResource)
                }

                confirmVerified(productsObserver)
            }

            productsCart.observeForTesting(productsCartObserver) {
                val cartSlot = slot<ProductsCart>()

                verify {
                    productsCartObserver.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    localProductsCartUseCase.invoke()
                }

                confirmVerified(productsCartObserver)

                Assert.assertEquals(
                    cartSlot.captured.size(),
                    mockNotEmptyProductCartSuccessResource.data?.size()
                )
            }
        }
    }

    @Test
    fun `market products requested with no error found when ViewModel is created and local product cart found`() {
        val productsObserver = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val productsCartObserver = mockk<Observer<ProductsCart>>(relaxed = true)

        coEvery {
            marketProductsUseCase.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListSuccessResource
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListSuccessResource)
            }

            products.observeForTesting(productsObserver) {
                verify {
                    productsObserver.onChanged(mockProductListSuccessResource)
                }

                confirmVerified(productsObserver)
            }

            productsCart.observeForTesting(productsCartObserver) {
                val cartSlot = slot<ProductsCart>()

                verify {
                    productsCartObserver.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    localProductsCartUseCase.invoke()
                }

                confirmVerified(productsCartObserver)

                Assert.assertEquals(
                    cartSlot.captured.size(),
                    mockNotEmptyProductCartSuccessResource.data?.size()
                )
            }
        }
    }

    @Test
    fun `market products requested but failed when ViewModel is created and no local products found`() {
        val productsObserver = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val productsCartObserver = mockk<Observer<ProductsCart>>(relaxed = true)
        val snackbarObserver = mockk<Observer<Event<String>>>(relaxed = true)

        coEvery {
            marketProductsUseCase(forceRefresh = false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListErrorResourceWithNoLocalData
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListErrorResourceWithNoLocalData)
            }

            products.observeForTesting(productsObserver) {
                verify {
                    productsObserver.onChanged(mockProductListErrorResourceWithNoLocalData)
                }

                confirmVerified(productsObserver)
            }

            productsCart.observeForTesting(productsCartObserver) {
                val cartSlot = slot<ProductsCart>()

                verify {
                    productsCartObserver.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    localProductsCartUseCase.invoke()
                }

                confirmVerified(productsCartObserver)

                Assert.assertEquals(cartSlot.captured.size(), 0)
            }

            snackbarErrorWithStringLiteral.observeForTesting(snackbarObserver) {
                verify {
                    snackbarObserver.onChanged(any())
                }

                confirmVerified(snackbarObserver)
            }
        }
    }

    @Test
    fun `market products requested but failed when ViewModel is created and local products found`() {
        val productsObserver = mockk<Observer<Resource<List<Product>>>>(relaxed = true)
        val productsCartObserver = mockk<Observer<ProductsCart>>(relaxed = true)
        val snackbarObserver = mockk<Observer<Event<String>>>(relaxed = true)

        coEvery {
            marketProductsUseCase(forceRefresh = false)
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListErrorResourceWithLocalData
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListErrorResourceWithLocalData)
            }

            products.observeForTesting(productsObserver) {
                verify {
                    productsObserver.onChanged(mockProductListErrorResourceWithLocalData)
                }

                confirmVerified(productsObserver)
            }

            productsCart.observeForTesting(productsCartObserver) {
                val cartSlot = slot<ProductsCart>()

                verify {
                    productsCartObserver.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    localProductsCartUseCase.invoke()
                }

                confirmVerified(productsCartObserver)

                Assert.assertEquals(
                    cartSlot.captured.size(), mockNotEmptyProductCartSuccessResource.data?.size()
                )
            }

            snackbarErrorWithStringLiteral.observeForTesting(snackbarObserver) {
                verify {
                    snackbarObserver.onChanged(any())
                }

                confirmVerified(snackbarObserver)
            }
        }
    }

    @Test
    fun `add product to products cart`() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)
        val mockProductList = MockDataProvider.createMockProductList()

        coEvery {
            marketProductsUseCase.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListSuccessResource
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        coEvery { saveLocalProductsCartUseCase.invoke(any()) } just Runs

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListSuccessResource)
            }
            productsCart.observeForTesting(observer) {
                addProductToCart(1, mockProductList.first())

                val cartSlot = slot<ProductsCart>()

                verify {
                    observer.onChanged(capture(cartSlot))
                }

                coVerify(exactly = 1) {
                    saveLocalProductsCartUseCase.invoke(any())
                    localProductsCartUseCase.invoke()
                }

                confirmVerified(observer)

                Assert.assertEquals(
                    cartSlot.captured.size(),
                    mockNotEmptyProductCartSuccessResource.data?.size()?.plus(1)
                )
            }
        }
    }

    @Test
    fun `navigate to checkout screen`() {
        coEvery {
            marketProductsUseCase.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListSuccessResource
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            products.getOrAwaitValue()
            productsCart.getOrAwaitValue() {
                checkLocalProductsCart(mockProductListSuccessResource)
            }
        }

        val event = Event(
            NavigationCommand.To(
                MarketProductsFragmentDirections.actionGoToCheckout(
                    mockNotEmptyProductCartSuccessResource.data ?: ProductsCart()
                )
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

        coEvery {
            marketProductsUseCase.invoke(any())
        } returns MutableLiveData<Resource<List<Product>>>().apply {
            value = mockProductListSuccessResource
        }

        coEvery {
            localProductsCartUseCase.invoke()
        } returns MutableLiveData<Resource<ProductsCart>>().apply {
            value = mockNotEmptyProductCartSuccessResource
        }

        marketProductsViewModel = MarketProductsViewModel(
            marketProductsUseCase,
            saveLocalProductsCartUseCase,
            localProductsCartUseCase,
            dispatchers
        )

        with(marketProductsViewModel) {
            refreshMarketProducts()

            products.observeForTesting(observer) {
                verify {
                    observer.onChanged(mockProductListSuccessResource) // When VM is created
                    observer.onChanged(mockProductListSuccessResource) // When VM is refreshed
                }

                confirmVerified(observer)
            }
        }
    }

}