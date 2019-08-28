package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import utils.loadImage

object MarketCheckoutFragmentBinding {

    @BindingAdapter("cartProductItems")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, cart: ProductsCart?) {
        with(recyclerView.adapter as MarketCheckoutAdapter) {
            cart?.let { setAdapterItems(it.getProducts() as LinkedHashMap<Product, Int>) }
        }
    }

    @BindingAdapter("cartProductImageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let { view.loadImage(it, R.drawable.ic_broken_image_black_24dp) }
    }

    @BindingAdapter("showIfCartProductWithDiscount")
    @JvmStatic
    fun showDiscountInfoView(view: View, productWithDiscount: Boolean) {
        view.visibility = if (productWithDiscount) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    @BindingAdapter("totalCartPrice")
    @JvmStatic
    fun setTotalCartPrice(view: TextView, cart: ProductsCart?) {
        val totalPrice = CurrencyAmount(cart?.getTotalAmount() ?: 0.00).formatCurrencyInLocale()

        view.text = totalPrice
    }

    @BindingAdapter("totalCartItems")
    @JvmStatic
    fun setTotalCartQuantity(view: TextView, cart: ProductsCart?) {
        val quantity = cart?.size() ?: 0

        view.text = view.context.getString(
            R.string.market_overview_fragment_bottom_sheet_subtotal_title,
            quantity
        )
    }

}