package com.dmanluc.cabifymarket.di

import android.content.res.AssetManager
import com.dmanluc.cabifymarket.data.local.typeadapter.InterfaceAdapter
import com.dmanluc.cabifymarket.data.local.typeadapter.RuntimeTypeAdapterFactory
import com.dmanluc.cabifymarket.data.remote.api.MarketApi
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSource
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSourceImpl
import com.dmanluc.cabifymarket.data.remote.mapper.ProductEntityMapper
import com.dmanluc.cabifymarket.domain.model.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.model.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.model.ProductDiscountRule
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Koin DI module for fetching remote data dependencies
 *
 */
fun createRemoteModule(baseUrl: String): Module = module {
    factory<Interceptor> {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory { OkHttpClient.Builder().addInterceptor(get()).build() }

    single {
        Retrofit.Builder().client(get()).baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
    }

    factory { get<Retrofit>().create(MarketApi::class.java) }

    factory { androidContext().assets as AssetManager }

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

    factory { ProductEntityMapper(get(), get()) }

    factory<MarketRemoteDataSource> {
        MarketRemoteDataSourceImpl(
            get(), get(), get(), get(), get()
        )
    }

}