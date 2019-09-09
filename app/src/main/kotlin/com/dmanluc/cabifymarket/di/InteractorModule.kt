package com.dmanluc.cabifymarket.di

import com.dmanluc.cabifymarket.domain.interactor.DeleteProductsCartInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetLastSavedProductsCartInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetProductsFromLocalDatabaseInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-28.
 */
val interactorModule: Module = module {
    factory { GetProductsInteractor(get()) }
    factory { GetProductsFromLocalDatabaseInteractor(get()) }
    factory { SaveProductsCartInteractor(get()) }
    factory { GetLastSavedProductsCartInteractor(get()) }
    factory { DeleteProductsCartInteractor(get()) }
}