package com.dmanluc.cabifymarket.presentation.feature.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmanluc.cabifymarket.databinding.FragmentMarketProductsBinding
import com.dmanluc.cabifymarket.presentation.base.BaseFragment
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketProductsFragment : BaseFragment() {

    private val viewModel: MarketProductsViewModel by viewModel()

    private lateinit var binding: FragmentMarketProductsBinding

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarketProductsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        binding.productsRecycler.adapter = MarketProductsAdapter { quantity, product ->
            viewModel.addProductToCart(quantity, product)
        }

        binding.cartCheckout.setOnClickListener {
            viewModel.goToCheckout()
        }

    }

}