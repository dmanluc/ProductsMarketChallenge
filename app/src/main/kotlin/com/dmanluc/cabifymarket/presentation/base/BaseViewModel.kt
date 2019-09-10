package com.dmanluc.cabifymarket.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.dmanluc.cabifymarket.presentation.navigation.NavigationCommand
import com.dmanluc.cabifymarket.utils.Event

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Base view model template for base fragment
 *
 */
abstract class BaseViewModel : ViewModel() {

    protected val _snackbarErrorWithStringResId: MutableLiveData<Event<Int>> = MutableLiveData()
    val snackbarErrorWithStringResId: LiveData<Event<Int>> get() = _snackbarErrorWithStringResId

    protected val _snackbarErrorWithStringLiteral: MutableLiveData<Event<String>> = MutableLiveData()
    val snackbarErrorWithStringLiteral: LiveData<Event<String>> get() = _snackbarErrorWithStringLiteral

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    /**
     * Handle navigation from a view model
     */
    fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }

}