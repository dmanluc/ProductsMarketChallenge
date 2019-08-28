package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.recyclerview.widget.DiffUtil
import com.dmanluc.cabifymarket.domain.entity.Product

class MarketProductItemDiffCallback(
    private val oldList: List<Product>,
    private val newList: List<Product>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].type == newList[newItemPosition].type
                && oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].imageUrl == newList[newItemPosition].imageUrl
                && oldList[oldItemPosition].price.amount == newList[newItemPosition].price.amount
    }
}