package com.genetic.salesman.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.genetic.salesman.DealerApplication
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.adapter.ProductAdapter
import com.genetic.salesman.adapter.ProductOptionAdapter
import com.genetic.salesman.databinding.FragmentProductBinding
import com.genetic.salesman.databinding.ProductOptionDialogBinding
import com.genetic.salesman.interfaces.ProductItemClickListener
import com.genetic.salesman.interfaces.ProductOptionListener
import com.genetic.salesman.model.*
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : Fragment(), ProductItemClickListener, ProductOptionListener {

    private var _binding : FragmentProductBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private var mProductCategoryDetail: ArrayList<String>? = null
    private var mProductItemClickListener: ProductItemClickListener? = null
    private var mProductOptionListener: ProductOptionListener? = null
    private var productOption: ProductOption? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProductBinding.inflate(inflater)
        preference = Preference(requireContext())
        mProductCategoryDetail = requireArguments().getStringArrayList(AppConstant.PRODUCT_CATEGORY)
        mProductItemClickListener = this
        mProductOptionListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).openDrawer() }
        binding.toolbarLayout.screenTitle.text = resources.getString(R.string.product_screen)
        binding.productRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        updateCartStatus()
        getProductListFromCategory()
    }

    private fun getProductListFromCategory() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface()
            .getProductList(preference?.getString(AppConstant.DEALER_ID, ""),
                mProductCategoryDetail!![0]
            )
            .enqueue(object : Callback<ProductListResponse> {
                override fun onResponse(
                    call: Call<ProductListResponse>,
                    response: Response<ProductListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        val meta = body.meta
                        if (meta.code.equals("200")) {
                            binding.productRecyclerview.adapter = ProductAdapter(body.data, mProductItemClickListener!!, requireContext())
                        } else {
                            showError(meta.message)
                        }

                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    showError("Error occurred!! Please try again later")
                    t.printStackTrace()
                }

            })
    }

    override fun onProductItemClick(productListItem: ProductListItem) {
        Log.d("niraj",productListItem.toString())
        getProductsOptionList(productListItem)
    }

    private fun getProductsOptionList(productListItem: ProductListItem) {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface()
            .getProductOptionList(preference?.getString(AppConstant.DEALER_ID, ""), productListItem.id.toString())
            .enqueue(object : Callback<ProductOptionListResponse> {
                override fun onResponse(call: Call<ProductOptionListResponse>,
                                        response: Response<ProductOptionListResponse>)
                {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        val meta = body.meta
                        if (meta.code.equals("200")) {
                            showProductOptionDialog(body.data, productListItem.image)
                        } else {
                            showError(meta.message)
                        }

                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<ProductOptionListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    showError("Error occurred!! Please try again later")
                    t.printStackTrace()
                }

            })
    }

    private fun showProductOptionDialog(productOptionList: ArrayList<ProductOption>, image: String) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding = ProductOptionDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        dialogBinding.rbtRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        dialogBinding.rbtRecycler.adapter = ProductOptionAdapter(productOptionList, mProductOptionListener!!, requireContext())
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnOk.setOnClickListener {
            if (productOption != null) {
                val productOptionKey: String = "${productOption?.productId}-${productOption?.productName}-${productOption?.optionTitle}-${productOption?.id}"
                val customProductModel = CustomProductOptionModel(productOption, 1, image)
                ((requireActivity() as MainActivity).applicationContext as DealerApplication).getProductCartList().putIfAbsent(productOptionKey, customProductModel)
                if(!((requireActivity() as MainActivity).applicationContext as DealerApplication).getKeyArrayList().contains(productOptionKey)) {
                    ((requireActivity() as MainActivity).applicationContext as DealerApplication).getKeyArrayList().add(productOptionKey)
                }
            }
            updateCartStatus()
            printItemsInCart()
            dialog.dismiss()
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun updateCartStatus() {
        if (((requireActivity() as MainActivity).applicationContext as DealerApplication).getProductCartList().isNotEmpty()) {
            binding.toolbarLayout.imgCart.visibility = View.VISIBLE
            binding.toolbarLayout.cartCount.visibility = View.VISIBLE
            binding.toolbarLayout.cartCount.text = ((requireActivity() as MainActivity).applicationContext as DealerApplication).getProductCartList().size.toString()
            binding.toolbarLayout.imgCart.setOnClickListener {
                (requireActivity() as MainActivity).openOtherFragment(CartFragment())
            }
        } else {
            binding.toolbarLayout.imgCart.visibility = View.GONE
            binding.toolbarLayout.cartCount.visibility = View.GONE
            binding.toolbarLayout.cartCount.text = ""
        }
        binding.submitCl.setOnClickListener {
            (requireActivity() as MainActivity).openOtherFragment(CartFragment())
        }
        loadTotals()
    }

    private fun printItemsInCart() {
        val cart = ((requireActivity() as MainActivity).applicationContext as DealerApplication).getProductCartList()
        for (index in cart.entries) {
            Log.d("Niraj","key: ${index.key}\n value: ${index.value.productOption}\n quantity: ${index.value.quantity}")
        }
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }

    override fun onProductOptionClick(productOption: ProductOption) {
        this.productOption = productOption
    }

    private fun loadTotals() {
        val cart = (requireContext().applicationContext as DealerApplication).getProductCartList()
        var subtotal = 0.0
        var size = 0
        for (index in cart.entries) {
            subtotal += (index.value.productOption.optionAmount * index.value.quantity)
            size += 1
        }
        binding.cartCounted.text = size.toString()
        binding.subtotal.text = subtotal.toString()
        if (cart.isEmpty()) {
            binding.submitCl.visibility = View.GONE
        } else {
            binding.submitCl.visibility = View.VISIBLE
        }

    }
}