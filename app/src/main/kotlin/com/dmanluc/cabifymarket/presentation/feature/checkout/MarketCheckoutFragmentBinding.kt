package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.domain.model.CurrencyAmount
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.utils.hide
import com.dmanluc.cabifymarket.utils.loadImage
import com.dmanluc.cabifymarket.utils.show

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Custom data bindings for checkout fragment
 *
 */
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
        if (productWithDiscount) view.show() else view.hide()
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
            R.string.market_overview_fragment_bottom_sheet_subtotal_title, quantity
        )
    }

    @BindingAdapter("enableCheckout")
    @JvmStatic
    fun setCheckoutButtonEnabled(view: Button, cart: ProductsCart?) {
        cart?.let {
            view.isEnabled = it.size() != 0
        } ?: run { view.isEnabled = false }
    }

    @BindingAdapter("showWhenEmptyCart")
    @JvmStatic
    fun setEmptyProductsCartInfoMessage(view: TextView, cart: ProductsCart?) {
        cart?.let {
            if (it.size() != 0) view.hide() else view.show()
        } ?: run { view.hide() }
    }

}