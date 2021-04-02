package com.genetic.salesman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.adapter.DealerListFragmentAdapter
import com.genetic.salesman.databinding.FragmentDealerListBinding
import com.genetic.salesman.interfaces.DealerListFragmentItemClickListener
import com.genetic.salesman.model.DealerFragmentItem
import com.genetic.salesman.model.DealerListFragmentResponse
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetDealerListFragment : Fragment(), DealerListFragmentItemClickListener {
    private var _binding: FragmentDealerListBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val dealerListFragmentItemClickListener: DealerListFragmentItemClickListener = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealerListBinding.inflate(inflater)
        binding.fab.setOnClickListener { openAddDealerFragment(null) }
        binding.fab.setOnLongClickListener { v ->
            v.setOnTouchListener { view, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        view.x = event.rawX + (binding.fab.layoutParams.width.div(2))
                        view.y = event.rawY + (binding.fab.layoutParams.height.div(2))
                    }
                    MotionEvent.ACTION_UP -> view.setOnTouchListener(null)
                    else -> {
                    }
                }
                true
            }
            true
        }
        return binding.root
    }

    private fun openAddDealerFragment(dealerFragmentItem: DealerFragmentItem?) {
        val addDealerFragment = AddDealerFragment()
        val bundle = Bundle()
        if (dealerFragmentItem != null) {
            bundle.putString("editDealerId", dealerFragmentItem.id.toString())
        }
        addDealerFragment.arguments = bundle
        (requireActivity() as MainActivity).openOtherFragment(addDealerFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = Preference(requireContext())
        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.toolbarLayout.screenTitle.text = "Dealer List"
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )
        callGetDealerListApi()
    }

    private fun callGetDealerListApi() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().getDealerFragmentList(preference?.getString(AppConstant.SALESMAN_ID, ""))
            .enqueue(object : Callback<DealerListFragmentResponse> {
                override fun onResponse(
                    call: Call<DealerListFragmentResponse>,
                    response: Response<DealerListFragmentResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            binding.dealerListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                            binding.dealerListRecyclerview.adapter = DealerListFragmentAdapter(body.data, dealerListFragmentItemClickListener, requireContext())
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<DealerListFragmentResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }

    override fun onDealerFragmentItemClick(dealerFragmentItem: DealerFragmentItem) {
        openAddDealerFragment(dealerFragmentItem)
    }
}