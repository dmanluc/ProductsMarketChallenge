package com.dmanluc.cabifymarket.presentation.feature.market

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import utils.loadImage

object MarketProductsFragmentBinding {

    @BindingAdapter("app:showWhenLoading")
    @JvmStatic
    fun <T>showWhenLoading(view: SwipeRefreshLayout, resource: Resource<T>?) {
        Log.d(MarketProductsFragmentBinding::class.java.simpleName, "Resource: $resource")
        if (resource != null) view.isRefreshing = resource.status == Resource.Status.LOADING
    }

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(recyclerView: RecyclerView, resource: Resource<List<Product>>?) {
        with(recyclerView.adapter as MarketProductsAdapter) {
            resource?.data?.let { setAdapterItems(it) }
        }
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic fun loadImage(view: ImageView, url: String) {
        view.loadImage(url, R.drawable.ic_error_black_24dp)
    }

    @BindingAdapter("app:showWhenEmptyList")
    @JvmStatic fun showMessageErrorWhenEmptyList(view: View, resource: Resource<List<Product>>?) {
        if (resource != null) {
            view.visibility = if (resource.status == Resource.Status.ERROR && resource.data?.isEmpty() != false) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}