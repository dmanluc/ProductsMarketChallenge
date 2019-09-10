package com.dmanluc.cabifymarket.data.local.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dmanluc.cabifymarket.data.local.dao.ShoppingCartDao
import com.dmanluc.cabifymarket.data.local.datasource.CacheDataSource
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSourceImpl
import com.dmanluc.cabifymarket.data.local.mapper.ProductsCartDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.local.mapper.ShoppingCartDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.remote.utils.CoroutinesMainDispatcherRule
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.utils.MockDataProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Daniel Manrique Lucas <dmanluc91></dmanluc91>@gmail.com>
 * @version 1
 * @since 2019-09-10.
 */
@ExperimentalCoroutinesApi
class ProductsCartLocalRepositoryImplTest {

    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule: CoroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    private lateinit var observer: Observer<Resource<ProductsCart>>
    private lateinit var repository: ProductsCartLocalRepository
    private lateinit var localDataSource: ProductsCartLocalDataSource
    private val databaseEntityToDomainMapper = mockk<ShoppingCartDatabaseEntityToDomainMapper>()
    private val domainToDatabaseEntityMapper = mockk<ProductsCartDomainToDatabaseEntityMapper>()
    private val productsCartDao = mockk<ShoppingCartDao>(relaxed = true)
    private val cacheService = mockk<CacheDataSource>(relaxed = true)

    @Before
    fun setUp() {
        observer = mockk(relaxed = true)

        localDataSource = ProductsCartLocalDataSourceImpl(
            productsCartDao,
            domainToDatabaseEntityMapper,
            databaseEntityToDomainMapper,
            cacheService
        )

        repository = ProductsCartLocalRepositoryImpl(localDataSource)
    }

    @Test
    fun `save products cart to local database`() {
        val mockProductsCart = MockDataProvider.createMockProductsCart()
        val mockProductsCartEntity = MockDataProvider.createMockProductsCartEntity()

        coEvery { productsCartDao.saveShoppingCart(any()) } just Runs
        every { domainToDatabaseEntityMapper.mapFrom(any()) } returns mockProductsCartEntity

        runBlocking {
            repository.saveLocalProductsCart(mockProductsCart)
        }

        verifyOrder {
            cacheService.save(mockProductsCart)
            domainToDatabaseEntityMapper.mapFrom(mockProductsCart)
        }

        coVerify(exactly = 1) {
            productsCartDao.saveShoppingCart(mockProductsCartEntity)
        }
    }

    @Test
    fun `get last saved products cart from cache`() {
        val mockProductsCart = MockDataProvider.createMockProductsCart()

        every { cacheService.get(ProductsCart::class.java) } returns mockProductsCart

        runBlocking {
            repository.getLocalProductsCart().observeForever(observer)
        }

        verifyOrder {
            cacheService.get(ProductsCart::class.java)
            observer.onChanged(Resource.success(mockProductsCart))
        }

        confirmVerified(observer)
    }

    @Test
    fun `get last saved products cart from local database`() {
        val mockProductsCartEntity = MockDataProvider.createMockProductsCartEntity()
        val mockProductsCart = MockDataProvider.createMockProductsCart()

        every { cacheService.get(ProductsCart::class.java) } returns null
        coEvery { productsCartDao.getShoppingCart() } returns mockProductsCartEntity
        every { databaseEntityToDomainMapper.mapFrom(any()) } returns mockProductsCart

        runBlocking {
            repository.getLocalProductsCart().observeForever(observer)
        }

        verifyOrder {
            cacheService.get(ProductsCart::class.java)
            databaseEntityToDomainMapper.mapFrom(mockProductsCartEntity)
            observer.onChanged(Resource.success(mockProductsCart))
        }

        coVerify(exactly = 1) {
            productsCartDao.getShoppingCart()
        }

        confirmVerified(observer)
    }

    @Test
    fun `delete products cart at local database`() {
        val mockProductsCart = MockDataProvider.createMockProductsCart()
        val mockProductsCartEntity = MockDataProvider.createMockProductsCartEntity()

        coEvery { productsCartDao.deleteShoppingCart(any()) } just Runs
        every { domainToDatabaseEntityMapper.mapFrom(any()) } returns mockProductsCartEntity

        runBlocking {
            repository.deleteLocalProductsCart(mockProductsCart)
        }

        verifyOrder {
            cacheService.clear()
            domainToDatabaseEntityMapper.mapFrom(mockProductsCart)
        }

        coVerify(exactly = 1) {
            productsCartDao.deleteShoppingCart(mockProductsCartEntity)
        }
        
    }

}