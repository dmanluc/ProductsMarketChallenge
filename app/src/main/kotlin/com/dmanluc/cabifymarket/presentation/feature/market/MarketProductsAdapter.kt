package com.dmanluc.cabifymarket.presentation.feature.market

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.databinding.ItemMarketProductBinding
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.utils.hide
import com.dmanluc.cabifymarket.utils.show
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import kotlinx.android.synthetic.main.item_market_product.view.*

class MarketProductsAdapter(private val onAddProductToCart: ((Int, Product) -> Unit)) :
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
        holder.bindTo(items[position])
    }

    override fun getItemCount() = items.size

    fun setAdapterItems(products: List<Product>) {
        val diffCallback = MarketProductItemDiffCallback(items, products)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items.apply {
            clear()
            addAll(products)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemMarketProductBinding.bind(itemView)

        fun bindTo(product: Product) {
            binding.product = product

            itemView.addToCart.setOnClickListener {
                onAddProductToCart(binding.productQuantity.value, product)
            }

            itemView.productPrice.text = "${product.price.formatCurrencyInLocale()} / ud."

            itemView.productQuantity.valueChangedListener = ValueChangedListener { value, _ ->
                when (product.discountRule) {
                    is BulkDiscountRule -> {
                        updateProductPriceForBulkDiscount(product, value)
                    }
                    is FreePerQuantityDiscountRule -> {
                        updateProductPriceForFreePerQuantityDiscount(product, value)
                    }
                    else -> hideOriginalPriceWithoutDiscount()
                }
            }
        }

        private fun updateProductPriceForBulkDiscount(product: Product, quantity: Int) {
            val discountRule = product.discountRule as BulkDiscountRule
            when {
                quantity == discountRule.buyQuantity -> {
                    showNewProductPriceWithDiscount(
                        product.providePriceWithDiscount(quantity),
                        product.price
                    )
                }
                quantity < discountRule.buyQuantity -> {
                    showOriginalProductPriceWithoutDiscount(product.price)
                }
            }
        }

        private fun showNewProductPriceWithDiscount(
            priceWithDiscount: CurrencyAmount,
            priceWithoutDiscount: CurrencyAmount
        ) {
            itemView.productsTotalPriceWithoutDiscount.text = SpannableString(
                "${priceWithoutDiscount.formatCurrencyInLocale()} / ud."
            ).apply {
                setSpan(
                    StrikethroughSpan(),
                    0,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            itemView.productsTotalPriceWithoutDiscount.show()
            itemView.productPrice.text = "${priceWithDiscount.formatCurrencyInLocale()} / ud."
        }

        private fun showOriginalProductPriceWithoutDiscount(originalProductPrice: CurrencyAmount) {
            itemView.productPrice.text = "${originalProductPrice.formatCurrencyInLocale()} / ud."
            itemView.productsTotalPriceWithoutDiscount.hide()
        }

        private fun updateProductPriceForFreePerQuantityDiscount(product: Product, quantity: Int) {
            val discountRule = product.discountRule as FreePerQuantityDiscountRule
            when {
                quantity <= discountRule.buyQuantity -> {
                    showOriginalProductPriceWithoutDiscount(product.price)
                }
                quantity.rem(discountRule.freeQuantity) >= 0 -> {
                    showNewProductPriceWithDiscount(
                        product.providePriceWithDiscount(quantity),
                        product.price
                    )
                }
            }
        }

        private fun hideOriginalPriceWithoutDiscount() {
            itemView.productsTotalPriceWithoutDiscount.hide()
        }

    }

}