package com.dmanluc.cabifymarket.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.dmanluc.cabifymarket.R

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        setContentView(R.layout.activity_market)

        supportActionBar?.apply {
            title = "Cabify Market"
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            show()
        }
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this, R.id.navHostFragment).navigateUp()

}