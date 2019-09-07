package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
val marketFeatureModule: Module = module {
    viewModel { MarketProductsViewModel(get(), get(), get(), get()) }
}