package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.data.local.AppDatabase
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSourceImpl
import com.dmanluc.cabifymarket.data.local.mapper.DatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.local.mapper.DomainToDatabaseEntityMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
private const val DATABASE = "DATABASE"

val localModule = module {

    single(named(DATABASE)) { AppDatabase.buildDatabase(androidContext()) }

    factory { get<AppDatabase>(named(DATABASE)).shoppingCartDao() }

    single { DomainToDatabaseEntityMapper() }

    single { DatabaseEntityToDomainMapper() }

    factory<ProductsCartLocalDataSource> { ProductsCartLocalDataSourceImpl(get(), get(), get()) }

}