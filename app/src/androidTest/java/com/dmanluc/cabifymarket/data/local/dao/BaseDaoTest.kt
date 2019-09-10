package com.dmanluc.cabifymarket.data.local.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dmanluc.cabifymarket.data.local.AppDatabase
import com.dmanluc.cabifymarket.data.local.typeadapter.InterfaceAdapter
import com.dmanluc.cabifymarket.data.local.typeadapter.RuntimeTypeAdapterFactory
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.ProductDiscountRule
import com.google.gson.GsonBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
abstract class BaseDaoTest : AutoCloseKoinTest() {

    companion object {
        private const val TEST_DATABASE = "DATABASE"
    }

    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var database: AppDatabase

    @Before
    open fun setUp() {
        this.configureDi()
    }

    private fun configureDi() {
        startKoin { modules(listOf(configureLocalModuleTest(ApplicationProvider.getApplicationContext()))) }
        database = get(named(TEST_DATABASE))
    }

    private fun configureLocalModuleTest(context: Context) = module {
        single(named(TEST_DATABASE)) {
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .build()
        }

        single {
            GsonBuilder().registerTypeAdapter(
                ProductDiscountRule::class.java, InterfaceAdapter<ProductDiscountRule>()
            ).setPrettyPrinting().create()
        }

        single(named("shoppingCartDatabaseConverterGson")) {
            GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory.of(ProductDiscountRule::class.java)
                        .registerSubtype(BulkDiscountRule::class.java)
                        .registerSubtype(FreePerQuantityDiscountRule::class.java)
                )
                .setPrettyPrinting()
                .create()
        }

        factory { (get<AppDatabase>(named(TEST_DATABASE))).marketProductsDao() }
        factory { (get<AppDatabase>(named(TEST_DATABASE))).shoppingCartDao() }

    }

}