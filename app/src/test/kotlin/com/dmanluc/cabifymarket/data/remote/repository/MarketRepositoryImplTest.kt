package com.dmanluc.cabifymarket.data.remote.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.local.mapper.ProductDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.remote.api.MarketApi
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSource
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSourceImpl
import com.dmanluc.cabifymarket.data.remote.mapper.ProductEntityMapper
import com.dmanluc.cabifymarket.data.remote.utils.CoroutinesMainDispatcherRule
import com.dmanluc.cabifymarket.data.remote.utils.MockDataProvider.createMockMarketProductsApiResponse
import com.dmanluc.cabifymarket.data.remote.utils.MockDataProvider.createMockProductList
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import io.mockk.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Daniel Manrique Lucas <dmanluc91></dmanluc91>@gmail.com>
 * @version 1
 * @since 2019-08-30.
 */
class MarketRepositoryImplTest {

    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule: CoroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    private lateinit var observer: Observer<Resource<List<Product>>>
    private lateinit var productsRepository: MarketRepository
    private lateinit var remoteDataSource: MarketRemoteDataSource
    //private lateinit var gson: Gson
    //private lateinit var assetManager: AssetManager
    private val entityMapper = mockk<ProductEntityMapper>()
    private val domainToDatabaseEntityMapper = ProductDomainToDatabaseEntityMapper()
    private val databaseEntityToDomainMapper = MarketProductDatabaseEntityToDomainMapper()
    private val marketService = mockk<MarketApi>()
    private val productsDao = mockk<MarketProductsDao>(relaxed = true)


    @Before
    fun setUp() {
        observer = mockk(relaxed = true)

        //gson = GsonBuilder().create()
        //assetManager = mockk()
        //mockkStatic("com.dmanluc.cabifymarket.utils.ExtensionsKt")
        //every { any<AssetManager>().readJsonAssetFileName(any()) } returns readJsonAsString("productDiscountRules.json")

        remoteDataSource = MarketRemoteDataSourceImpl(
            marketService,
            entityMapper,
            domainToDatabaseEntityMapper,
            databaseEntityToDomainMapper,
            productsDao
        )
        productsRepository = MarketRepositoryImpl(remoteDataSource)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `get products from remote API when no internet is available and no data saved in local database`() {
        val exception = Exception("No internet")
        every { marketService.getProductsAsync() } throws exception
        coEvery { productsDao.getMarketProducts() } returns listOf()

        runBlocking {
            productsRepository.getProducts().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading())
            observer.onChanged(Resource.loading(listOf()))
            observer.onChanged(Resource.error(exception, listOf()))
        }

        confirmVerified(observer)
    }

    @Test
    fun `get products from remote API and save them to local database`() {
        val mockProducts = createMockProductList()
        val mockProductsEntities = mockProducts.map { domainToDatabaseEntityMapper.mapFrom(it) }
        every { marketService.getProductsAsync() } returns GlobalScope.async { createMockMarketProductsApiResponse() }
        every { entityMapper.mapFrom(any()) } returns mockProducts
        coEvery { productsDao.getMarketProducts() } returns listOf() andThen mockProductsEntities

        runBlocking {
            productsRepository.getProducts().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading())
            observer.onChanged(Resource.loading(listOf()))
            observer.onChanged(Resource.success(mockProducts))
        }

        coVerify(exactly = 1) {
            productsDao.save(mockProductsEntities)
        }

        confirmVerified(observer)
    }

    @Test
    fun `get products from local database`() {
        val mockProducts = createMockProductList()
        val mockProductsEntities = mockProducts.map { domainToDatabaseEntityMapper.mapFrom(it) }
        every { marketService.getProductsAsync() } returns GlobalScope.async { createMockMarketProductsApiResponse() }
        every { entityMapper.mapFrom(any()) } returns mockProducts
        coEvery { productsDao.getMarketProducts() } returns mockProductsEntities

        runBlocking {
            productsRepository.getProducts().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading())
            observer.onChanged(Resource.success(mockProducts))
        }

        confirmVerified(observer)
    }

}