package com.dmanluc.cabifymarket.data.local.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.datasource.CacheDataSource
import com.dmanluc.cabifymarket.data.local.datasource.MarketProductsLocalDataSource
import com.dmanluc.cabifymarket.data.local.datasource.MarketProductsLocalDataSourceImpl
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.remote.utils.CoroutinesMainDispatcherRule
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.repository.MarketProductsLocalRepository
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version 1
 * @since 2019-09-10.
 */
@ExperimentalCoroutinesApi
class MarketProductsLocalRepositoryImplTest {

    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule: CoroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    private lateinit var observer: Observer<Resource<List<Product>>>
    private lateinit var repository: MarketProductsLocalRepository
    private lateinit var localDataSource: MarketProductsLocalDataSource
    private val databaseEntityToDomainMapper = mockk<MarketProductDatabaseEntityToDomainMapper>()
    private val productsDao = mockk<MarketProductsDao>(relaxed = true)
    private val cacheService = mockk<CacheDataSource>(relaxed = true)

    @Before
    fun setUp() {
        observer = mockk(relaxed = true)

        localDataSource = MarketProductsLocalDataSourceImpl(
            productsDao,
            databaseEntityToDomainMapper,
            cacheService
        )

        repository = MarketProductsLocalRepositoryImpl(localDataSource)
    }

    @Test
    fun `get products from local database`() {
        val mockProducts = MockDataProvider.createMockProductList()
        val mockProductsEntities = MockDataProvider.createMockProductEntities()

        coEvery { productsDao.getMarketProducts() } returns mockProductsEntities
        every { databaseEntityToDomainMapper.mapFrom(mockProductsEntities[0]) } returns mockProducts[0]
        every { databaseEntityToDomainMapper.mapFrom(mockProductsEntities[1]) } returns mockProducts[1]
        every { databaseEntityToDomainMapper.mapFrom(mockProductsEntities[2]) } returns mockProducts[2]

        runBlocking {
            repository.getLocalProducts().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.success(mockProducts))
        }

        coVerify(exactly = 1) {
            productsDao.getMarketProducts()
        }

        confirmVerified(observer)
    }

}