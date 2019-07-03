package com.dmanluc.cabifymarket.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
abstract class BaseFragment: Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    abstract fun getViewModel(): BaseViewModel

}