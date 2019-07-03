package com.dmanluc.cabifymarket.presentation

import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductsFragment

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
        switchFragment(MarketProductsFragment.newInstance())
    }

    private fun setupUi() {
        setContentView(R.layout.base_activity_with_fragment)

        supportActionBar?.apply {
            title = "Cabify Market"
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            show()
        }
    }

    private fun switchFragment(@NonNull fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment, fragment.javaClass.simpleName)

            if (addToBackStack) {
                addToBackStack(fragment.javaClass.simpleName)
            }

            commit()
        }

        Log.d(MarketActivity::class.java.canonicalName, "Switch to fragment [$fragment]")
    }

}