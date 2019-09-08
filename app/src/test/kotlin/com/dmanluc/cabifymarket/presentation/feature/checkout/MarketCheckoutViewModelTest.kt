package com.dmanluc.cabifymarket.presentation.feature.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.data.remote.utils.observeForTesting
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
 * @since 2019-09-05.
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class MarketCheckoutViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var marketCheckoutViewModel: MarketCheckoutViewModel

    @Before
    fun setUp() {
        marketCheckoutViewModel = MarketCheckoutViewModel()

        val mockProductsCart = MockDataProvider.createMockProductsCart()
        marketCheckoutViewModel.loadCartProducts(mockProductsCart)
    }

    @Test
    fun loadCartProducts() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)

        marketCheckoutViewModel.productsCart.observeForTesting(observer) {
            val cartSlot = slot<ProductsCart>()

            verify {
                observer.onChanged(capture(cartSlot))
            }

            confirmVerified(observer)

            Assert.assertEquals(cartSlot.captured.size(), 6)
        }
    }

    @Test
    fun updateProductCartQuantity() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)
        val mockProduct = MockDataProvider.createMockProductList().first()

        with(marketCheckoutViewModel) {
            productsCart.observeForTesting(observer) {
                updateProductCartQuantity(2, mockProduct)

                val cartSlot = slot<ProductsCart>()

                verify {
                    observer.onChanged(capture(cartSlot))
                }

                confirmVerified(observer)

                Assert.assertEquals(cartSlot.captured.size(), 7)
            }
        }
    }

    @Test
    fun removeProductFromCart() {
        val observer = mockk<Observer<ProductsCart>>(relaxed = true)
        val mockProduct = MockDataProvider.createMockProductList().first()

        with(marketCheckoutViewModel) {
            productsCart.observeForTesting(observer) {
                removeProductFromCart(mockProduct)

                val cartSlot = slot<ProductsCart>()

                verify {
                    observer.onChanged(capture(cartSlot))
                }

                confirmVerified(observer)

                Assert.assertEquals(cartSlot.captured.size(), 5)
            }
        }
    }

}