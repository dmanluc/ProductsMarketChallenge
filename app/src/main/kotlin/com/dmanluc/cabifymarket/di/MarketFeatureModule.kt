package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.domain.interactor.GetLastSavedProductsCartInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
val marketFeatureModule = module {
    factory { GetProductsInteractor(get()) }
    factory { SaveProductsCartInteractor(get()) }
    factory { GetLastSavedProductsCartInteractor(get()) }

    viewModel { MarketProductsViewModel(get(), get(), get()) }
}