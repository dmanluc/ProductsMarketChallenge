package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.domain.usecase.DeleteLocalProductsCartUseCase
import com.dmanluc.cabifymarket.presentation.feature.checkout.MarketCheckoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Koin DI module for market checkout dependencies
 *
 */
val marketCheckoutModule: Module = module {
    factory { DeleteLocalProductsCartUseCase(get()) }
    viewModel { MarketCheckoutViewModel(get()) }
}