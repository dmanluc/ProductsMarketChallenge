package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.remote.repository.MarketRepositoryImpl
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<MarketRepository> { MarketRepositoryImpl(get()) }
}