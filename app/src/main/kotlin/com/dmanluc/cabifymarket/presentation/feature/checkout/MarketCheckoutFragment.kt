package com.dmanluc.cabifymarket.presentation.feature.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.test.espresso.idling.CountingIdlingResource
import com.dmanluc.cabifymarket.databinding.FragmentMarketCheckoutBinding
import com.dmanluc.cabifymarket.presentation.base.BaseFragment
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import com.dmanluc.cabifymarket.utils.morphDoneAndRevert
import com.dmanluc.cabifymarket.utils.observeEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-20.
 *
 * Second flow fragment which shows checkout process for user products cart and perform fake payment process response
 *
 */
class MarketCheckoutFragment : BaseFragment() {

    private val viewModel: MarketCheckoutViewModel by viewModel()
    private val args: MarketCheckoutFragmentArgs by navArgs()

    val countingIdlingResource = CountingIdlingResource("MarketCheckoutFragment")

    private lateinit var binding: FragmentMarketCheckoutBinding

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val layoutParams =
                        binding.cartProductsRecycler.layoutParams as CoordinatorLayout.LayoutParams
                    layoutParams.height =
                        bottomSheet.top + binding.cartProductsRecycler.paddingBottom
                    binding.cartProductsRecycler.apply {
                        setLayoutParams(layoutParams)
                        requestLayout()
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED ->
                            if (countingIdlingResource.isIdleNow.not()) countingIdlingResource.decrement()
                        BottomSheetBehavior.STATE_COLLAPSED ->
                            if (countingIdlingResource.isIdleNow.not()) countingIdlingResource.decrement()
                        BottomSheetBehavior.STATE_DRAGGING ->
                            countingIdlingResource.increment()
                        else -> {
                        }
                    }
                }
            })
    }

    private fun configureCartPaymentButton() {
        viewModel.finishCheckoutFlow.observeEvent(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.cartPayment.run {
            setOnClickListener {
                // In a real context, when clicked it should launch a API call or whatever from view model to perform payment process.
                activity?.let { context ->
                    morphDoneAndRevert(context) { viewModel.closeFlow() }
                }
            }
        }
    }

}