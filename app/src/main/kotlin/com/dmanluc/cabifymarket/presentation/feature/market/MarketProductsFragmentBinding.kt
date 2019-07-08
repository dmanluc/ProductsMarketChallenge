package com.dmanluc.cabifymarket.presentation.feature.market

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import utils.hide
import utils.loadImage

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
        url?.let { view.loadImage(it, R.drawable.ic_error_black_24dp) }
    }

    @BindingAdapter("showWhenEmptyList")
    @JvmStatic
    fun showMessageErrorWhenEmptyList(view: View, resource: Resource<List<Product>>?) {
        if (resource != null) {
            view.visibility = if (resource.status == Resource.Status.ERROR && resource.data?.isEmpty() != false) {
                View.VISIBLE
            } else {
                View.GONE
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