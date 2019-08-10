package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.local.repository.ProductsCartRepositoryImpl
import com.dmanluc.cabifymarket.data.remote.repository.MarketRepositoryImpl
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.domain.repository.ProductsCartRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<MarketRepository> { MarketRepositoryImpl(get()) }
    factory<ProductsCartRepository> { ProductsCartRepositoryImpl(get()) }
}