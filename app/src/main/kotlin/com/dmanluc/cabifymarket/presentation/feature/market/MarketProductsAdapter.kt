package com.dmanluc.cabifymarket.presentation.feature.market

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.databinding.ItemMarketProductBinding
import com.dmanluc.cabifymarket.domain.entity.Product

class MarketProductsAdapter(private val viewModel: MarketProductsViewModel) :
    RecyclerView.Adapter<MarketProductsAdapter.ProductViewHolder>() {

    private val items: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_market_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindTo(items[position], viewModel)
    }

    override fun getItemCount() = items.size

    fun setAdapterItems(products: List<Product>) {
        val diffCallback = MarketProductItemDiffCallback(products, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.apply {
            clear()
            addAll(products)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMarketProductBinding.bind(itemView)

        fun bindTo(product: Product, viewModel: MarketProductsViewModel) {
            binding.product = product
            binding.viewmodel = viewModel
        }

    }

}