package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.local.repository.MarketProductsLocalRepositoryImpl
import com.dmanluc.cabifymarket.data.local.repository.ProductsCartLocalRepositoryImpl
import com.dmanluc.cabifymarket.data.remote.repository.MarketRepositoryImpl
import com.dmanluc.cabifymarket.domain.repository.MarketProductsLocalRepository
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    factory<MarketRepository> { MarketRepositoryImpl(get()) }
    factory<MarketProductsLocalRepository> { MarketProductsLocalRepositoryImpl(get()) }
    factory<ProductsCartLocalRepository> { ProductsCartLocalRepositoryImpl(get()) }
}