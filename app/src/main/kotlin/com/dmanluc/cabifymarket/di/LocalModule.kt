package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.local.AppDatabase
import com.dmanluc.cabifymarket.data.local.datasource.CacheDataSource
import com.dmanluc.cabifymarket.data.local.datasource.CacheDataSourceImpl
import com.dmanluc.cabifymarket.data.local.datasource.MarketProductsLocalDataSource
import com.dmanluc.cabifymarket.data.local.datasource.MarketProductsLocalDataSourceImpl
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSourceImpl
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.local.mapper.ProductDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.local.mapper.ProductsCartDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.local.mapper.ShoppingCartDatabaseEntityToDomainMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
private const val DATABASE = "DATABASE"

val localModule: Module = module {

    single(named(DATABASE)) { AppDatabase.buildDatabase(androidContext()) }

    factory { get<AppDatabase>(named(DATABASE)).shoppingCartDao() }

    factory { get<AppDatabase>(named(DATABASE)).marketProductsDao() }

    single { ProductsCartDomainToDatabaseEntityMapper() }

    single { ShoppingCartDatabaseEntityToDomainMapper() }

    single { ProductDomainToDatabaseEntityMapper() }

    single { MarketProductDatabaseEntityToDomainMapper() }

    factory<CacheDataSource> { CacheDataSourceImpl() }

    factory<ProductsCartLocalDataSource> {
        ProductsCartLocalDataSourceImpl(get(), get(), get(), get())
    }

    factory<MarketProductsLocalDataSource> {
        MarketProductsLocalDataSourceImpl(get(), get(), get())
    }

}