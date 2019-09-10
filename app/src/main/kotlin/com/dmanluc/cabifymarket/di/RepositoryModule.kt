package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.local.repository.MarketProductsLocalRepositoryImpl
import com.dmanluc.cabifymarket.data.local.repository.ProductsCartLocalRepositoryImpl
import com.dmanluc.cabifymarket.data.remote.repository.MarketRepositoryImpl
import com.dmanluc.cabifymarket.domain.repository.MarketProductsLocalRepository
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.utils.AppDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Koin DI module for repository dependencies
 *
 */
val repositoryModule: Module = module {
    single { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    single<MarketRepository> { MarketRepositoryImpl(get()) }
    single<MarketProductsLocalRepository> { MarketProductsLocalRepositoryImpl(get()) }
    single<ProductsCartLocalRepository> { ProductsCartLocalRepositoryImpl(get()) }
}