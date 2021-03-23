package com.genetic.salesman.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.databinding.FragmentChangePasswordBinding
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils

class ChangePasswordFragment : Fragment() {

    private var _binding : FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater)
        preference = Preference(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        binding.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
    }

    /*private fun getProductListFromCategory() {
         Utils.showProgress(requireContext())
         APIClient.getApiInterface()
             .getProductList(preference?.getString(AppConstant.DEALER_ID, ""),

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
     }*/

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }
}