package com.dmanluc.cabifymarket.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.dmanluc.cabifymarket.presentation.navigation.NavigationCommand
import com.dmanluc.cabifymarket.utils.Event

abstract class BaseViewModel : ViewModel() {

    protected val _snackbarErrorWithStringResId = MutableLiveData<Event<Int>>()
    val snackbarErrorWithStringResId: LiveData<Event<Int>> get() = _snackbarErrorWithStringResId

    protected val _snackbarErrorWithStringLiteral = MutableLiveData<Event<String>>()
    val snackbarErrorWithStringLiteral: LiveData<Event<String>> get() = _snackbarErrorWithStringLiteral

    // FOR NAVIGATION
    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }

}