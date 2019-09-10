package com.dmanluc.cabifymarket.presentation.navigation

import androidx.navigation.NavDirections

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Sealed class to handle more properly navigation from a view model
 *
 */
sealed class NavigationCommand {

    data class To(val directions: NavDirections) : NavigationCommand()
    object Back : NavigationCommand()
}