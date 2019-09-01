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
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.presentation.feature.market.MarketProductItemDiffCallback
import com.dmanluc.cabifymarket.utils.hide
import com.dmanluc.cabifymarket.utils.show
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import kotlinx.android.synthetic.main.item_checkout_product.view.*

class MarketCheckoutAdapter(private val onProductQuantityChanged: ((Int, Product) -> Unit),
                            private val onRemoveProductFromCart: ((Product) -> Unit)) :
    RecyclerView.Adapter<MarketCheckoutAdapter.ProductViewHolder>() {

    private var items: LinkedHashMap<Product, Int> = linkedMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_checkout_product, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindTo(Pair(items.keys.toList()[position], items.values.toList()[position]))
    }

    override fun getItemCount(): Int = items.size

    fun setAdapterItems(productsMap: LinkedHashMap<Product, Int>) {
        val diffCallback =
            MarketProductItemDiffCallback(items.keys.toList(), productsMap.keys.toList())
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

            itemView.productsTotalPrice.text =
                product.provideTotalPrice(quantity).formatCurrencyInLocale()
            itemView.productQuantity.value = quantity

            checkForProductDiscountEligible(product, quantity)

            itemView.productQuantity.valueChangedListener = ValueChangedListener { value, _ ->
                onProductQuantityChanged(value, product)

                updateDiscountInfoWhenQuantityUpdated(product, value)
            }
        }

        private fun checkForProductDiscountEligible(product: Product, quantity: Int) {
            val isEligibleForDiscount = when (product.discountRule) {
                is BulkDiscountRule -> {
                    quantity >= product.discountRule.buyQuantity
                }
                is FreePerQuantityDiscountRule -> {
                    quantity >= product.discountRule.freeQuantity
                }
                else -> false
            }

            if (isEligibleForDiscount) updateDiscountInfoWhenQuantityUpdated(product, quantity)
        }

        private fun updateDiscountInfoWhenQuantityUpdated(product: Product, newQuantity: Int) {
            when (product.discountRule) {
                is BulkDiscountRule -> {
                    updateProductsTotalPriceForBulkDiscount(product, newQuantity)
                }
                is FreePerQuantityDiscountRule -> {
                    updateProductsTotalPriceForFreePerQuantityDiscount(product, newQuantity)
                }
                else -> hideOriginalTotalPriceWithoutDiscount()
            }
        }

        private fun updateProductsTotalPriceForBulkDiscount(product: Product, quantity: Int) {
            val discountRule = product.discountRule as BulkDiscountRule
            when {
                quantity == discountRule.buyQuantity -> {
                    showNewProductsTotalPriceWithDiscount(
                        product.provideTotalPrice(quantity),
                        CurrencyAmount(product.price.amount * quantity)
                    )
                }
                quantity < discountRule.buyQuantity -> {
                    showOriginalProductsTotalPriceWithoutDiscount(CurrencyAmount(product.price.amount * quantity))
                }
            }
        }

        private fun showNewProductsTotalPriceWithDiscount(totalPriceWithDiscount: CurrencyAmount,
                                                          totalPriceWithoutDiscount: CurrencyAmount) {
            itemView.productsTotalPriceWithoutDiscount.text = SpannableString(
                totalPriceWithoutDiscount.formatCurrencyInLocale()
            ).apply {
                setSpan(
                    StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            itemView.productsTotalPriceWithoutDiscount.show()
            itemView.productsTotalPrice.text = totalPriceWithDiscount.formatCurrencyInLocale()
        }

        private fun showOriginalProductsTotalPriceWithoutDiscount(originalProductsTotalPrice: CurrencyAmount) {
            itemView.productsTotalPrice.text = originalProductsTotalPrice.formatCurrencyInLocale()
            itemView.productsTotalPriceWithoutDiscount.hide()
        }

        private fun updateProductsTotalPriceForFreePerQuantityDiscount(product: Product,
                                                                       quantity: Int) {
            val discountRule = product.discountRule as FreePerQuantityDiscountRule
            when {
                quantity <= discountRule.buyQuantity -> {
                    showOriginalProductsTotalPriceWithoutDiscount(CurrencyAmount(product.price.amount * quantity))
                }
                quantity.rem(discountRule.freeQuantity) >= 0 -> {
                    showNewProductsTotalPriceWithDiscount(
                        product.provideTotalPrice(quantity),
                        CurrencyAmount(product.price.amount * quantity)
                    )
                }
            }
        }

        private fun hideOriginalTotalPriceWithoutDiscount() {
            itemView.productsTotalPriceWithoutDiscount.hide()
        }

    }


}