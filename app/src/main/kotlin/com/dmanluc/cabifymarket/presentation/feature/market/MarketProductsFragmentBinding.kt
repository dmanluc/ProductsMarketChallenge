package com.dmanluc.cabifymarket.presentation.feature.market

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.domain.model.CurrencyAmount
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.utils.hide
import com.dmanluc.cabifymarket.utils.loadImage
import com.dmanluc.cabifymarket.utils.orFalse
import com.dmanluc.cabifymarket.utils.show

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Custom data bindings for market products fragment
 *
 */
object MarketProductsFragmentBinding {

    @BindingAdapter("showWhenLoading")
    @JvmStatic
    fun <T> showWhenLoading(view: SwipeRefreshLayout, resource: Resource<T>?) {
        Log.d(MarketProductsFragmentBinding::class.java.simpleName, "Resource: $resource")
        if (resource != null) view.isRefreshing = resource.status == Resource.Status.LOADING
    }

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, resource: Resource<List<Product>>?) {
        with(recyclerView.adapter as MarketProductsAdapter) {
            resource?.data?.let { setAdapterItems(it) }
        }
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let { view.loadImage(it, errorResource = R.drawable.ic_broken_image_black_24dp) }
    }

    @BindingAdapter("showWhenEmptyList")
    @JvmStatic
    fun showMessageErrorWhenEmptyList(view: ImageView, resource: Resource<List<Product>>?) {
        if (resource != null) {
            when {
                resource.status != Resource.Status.LOADING && resource.data?.isEmpty().orFalse() -> {
                    view.show()
                }
                else -> view.hide()
            }
        } else {
            view.hide()
        }
    }

    @BindingAdapter("showWhenEmptyList")
    @JvmStatic
    fun showMessageErrorWhenEmptyList(view: TextView, resource: Resource<List<Product>>?) {
        if (resource != null) {
            when {
                resource.status == Resource.Status.ERROR && resource.data?.isEmpty() ?: false -> {
                    view.apply {
                        show()
                        text = resources.getString(R.string.general_error_message)
                    }
                }
                resource.status == Resource.Status.SUCCESS && resource.data?.isEmpty() ?: false -> {
                    view.apply {
                        show()
                        text =
                            resources.getString(R.string.market_overview_fragment_products_not_available)
                    }
                }
                else -> view.hide()
            }
        } else {
            view.hide()
        }
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
            view.text = view.resources.getString(R.string.market_overview_fragment_checkout_cart_button, quantity, totalPrice)
        } else {
            view.text = view.resources.getString(R.string.market_overview_fragment_initial_cart_checkout_button)
        }
    }

    @BindingAdapter("showIfProductWithDiscount")
    @JvmStatic
    fun showDiscountInfoView(view: View, productWithDiscount: Boolean) {
        if (productWithDiscount) view.show() else view.hide()
    }

}