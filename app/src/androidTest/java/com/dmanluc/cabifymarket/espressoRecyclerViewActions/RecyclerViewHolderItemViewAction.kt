package com.dmanluc.cabifymarket.espressoRecyclerViewActions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-09-08
 *
 * Espresso view action to perform click on a specific view in a recycler view holder
 *
 */
class RecyclerViewHolderItemViewAction private constructor(private val childItemViewId: Int) :
    ViewAction {

    override fun getDescription(): String {
        return "Click on a child view with specified id";
    }

    override fun getConstraints(): Matcher<View>? {
        return null
    }

    override fun perform(uiController: UiController?, view: View?) {
        val v: View? = view?.findViewById(childItemViewId)
        v?.performClick()
    }

    companion object {

        fun withChildItemId(childItemViewId: Int): RecyclerViewHolderItemViewAction {
            return RecyclerViewHolderItemViewAction(childItemViewId)
        }

    }

}