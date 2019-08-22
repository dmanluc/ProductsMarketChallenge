package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import utils.loadImage

object MarketCheckoutFragmentBinding {

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, resource: Resource<List<Product>>?) {
        with(recyclerView.adapter as MarketCheckoutAdapter) {
            resource?.data?.let { setAdapterItems(it) }
        }
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let { view.loadImage(it, R.drawable.ic_broken_image_black_24dp) }
    }

    @BindingAdapter("buttonText")
    @JvmStatic
    fun setButtonTextInfo(view: Button, cart: ProductsCart?) {
        cart?.let {
            view.isEnabled = it.size() != 0
        } ?: run { view.isEnabled = false }

        val quantity = cart?.size() ?: 0
        val totalPrice = CurrencyAmount(cart?.getTotalAmount() ?: 0.00).formatCurrencyInLocale()

        if (quantity != 0) {
            view.text = "Checkout $quantity items = $totalPrice"
        } else {
            view.text = "Cesta vacía"
        }
    }

    @BindingAdapter("showIfProductWithDiscount")
    @JvmStatic
    fun showDiscountInfoView(view: View, productWithDiscount: Boolean) {
        view.visibility = if (productWithDiscount) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}