package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.databinding.ItemCheckoutProductBinding
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductItemDiffCallback
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import kotlinx.android.synthetic.main.item_checkout_product.view.*
import kotlinx.android.synthetic.main.item_market_product.view.productPrice
import kotlinx.android.synthetic.main.item_market_product.view.productPriceWithoutDiscount
import kotlinx.android.synthetic.main.item_market_product.view.productQuantity
import utils.hide
import utils.show

class MarketCheckoutAdapter(
    private val onProductQuantityChanged: ((Int, Product) -> Unit),
    private val onRemoveProductFromCart: ((Product) -> Unit)
) :
    RecyclerView.Adapter<MarketCheckoutAdapter.ProductViewHolder>() {

    private var items: LinkedHashMap<Product, Int> = linkedMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_checkout_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindTo(Pair(items.keys.toList()[position], items.values.toList()[position]))
    }

    override fun getItemCount(): Int = items.size

    fun setAdapterItems(productsMap: LinkedHashMap<Product, Int>) {
        val diffCallback =
            MarketProductItemDiffCallback(productsMap.keys.toList(), items.keys.toList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = productsMap

        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemCheckoutProductBinding.bind(itemView)

        fun bindTo(productsMapEntry: Pair<Product, Int>) {
            val product = productsMapEntry.first
            val quantity = productsMapEntry.second

            binding.product = productsMapEntry.first

            itemView.removeProductFromCart.setOnClickListener {
                items.remove(product)
                notifyItemRemoved(adapterPosition)
                onRemoveProductFromCart(product)
            }

            itemView.productPrice.text = product.currencyAmount.formatCurrencyInLocale()
            itemView.productQuantity.value = quantity
            itemView.productQuantity.valueChangedListener = ValueChangedListener { value, _ ->
                onProductQuantityChanged(value, product)

                when (product.discountRule) {
                    is BulkDiscountRule -> {
                        updateProductPricesForBulkDiscount(product, value)
                    }
                    else -> hideOriginalPriceWithoutDiscount()
                }
            }
        }

        private fun updateProductPricesForBulkDiscount(product: Product, quantity: Int) {
            val discountRule = product.discountRule as BulkDiscountRule
            when {
                quantity == discountRule.buyQuantity -> {
                    itemView.productPriceWithoutDiscount.text =
                        SpannableString(product.currencyAmount.formatCurrencyInLocale()).apply {
                            setSpan(
                                StrikethroughSpan(),
                                0,
                                length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    itemView.productPriceWithoutDiscount.show()
                    itemView.productPrice.text =
                        CurrencyAmount(product.discountRule.priceWithDiscount).formatCurrencyInLocale()
                }
                quantity < discountRule.buyQuantity -> {
                    itemView.productPrice.text = product.currencyAmount.formatCurrencyInLocale()
                    itemView.productPriceWithoutDiscount.hide()
                }
            }
        }

        private fun hideOriginalPriceWithoutDiscount() {
            itemView.productPriceWithoutDiscount.hide()
        }

    }

}