package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.navArgs
import com.dmanluc.cabifymarket.databinding.FragmentMarketCheckoutBinding
import com.dmanluc.cabifymarket.presentation.base.BaseFragment
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import com.dmanluc.cabifymarket.utils.morphDoneAndRevert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-20.
 */
class MarketCheckoutFragment : BaseFragment() {

    private val viewModel: MarketCheckoutViewModel by viewModel()
    private val args: MarketCheckoutFragmentArgs by navArgs()

    private lateinit var binding: FragmentMarketCheckoutBinding

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMarketCheckoutBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()
        
        viewModel.loadCartProducts(args.productsCart)
    }

    private fun setupUI() {
        configureRecyclerView()
        configureBottomSheet()
        configureCartPaymentButton()
    }

    private fun configureRecyclerView() {
        binding.cartProductsRecycler.adapter =
            MarketCheckoutAdapter(onProductQuantityChanged = { newQuantity, product ->
                viewModel.updateProductCartQuantity(newQuantity, product)
            }, onRemoveProductFromCart = {
                viewModel.removeProductFromCart(it)
            })
    }

    private fun configureBottomSheet() {
        BottomSheetBehavior.from(binding.checkoutOrderInfoBottomSheet)
            .setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                    val layoutParams =
                        binding.cartProductsRecycler.layoutParams as CoordinatorLayout.LayoutParams
                    layoutParams.height = p0.top + binding.cartProductsRecycler.paddingBottom
                    binding.cartProductsRecycler.apply {
                        setLayoutParams(layoutParams)
                        requestLayout()
                    }
                }

                override fun onStateChanged(p0: View, p1: Int) {}
            })
    }

    private fun configureCartPaymentButton() {

        binding.cartPayment.run {
            setOnClickListener {
                activity?.let { context ->
                    morphDoneAndRevert(context) {  }
                }
            }
        }
    }

}