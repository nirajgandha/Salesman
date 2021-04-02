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
import com.genetic.salesman.adapter.DailyReportListFragmentAdapter
import com.genetic.salesman.databinding.FragmentDailyReportListBinding
import com.genetic.salesman.interfaces.DailyReportListFragmentItemClickListener
import com.genetic.salesman.model.DailyReportListItem
import com.genetic.salesman.model.DailyReportListResponse
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetDailyReportListFragment : Fragment(), DailyReportListFragmentItemClickListener {
    private var _binding: FragmentDailyReportListBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val dailyReportListFragmentItemClickListener: DailyReportListFragmentItemClickListener = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReportListBinding.inflate(inflater)
        binding.fab.setOnClickListener { openAddDailyReportToAdmin(null) }
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

    private fun openAddDailyReportToAdmin(dailyReportListItem: DailyReportListItem?) {
        val addDailyReportToAdmin = DailyReportToAdmin()
        val bundle = Bundle()
        if (dailyReportListItem != null) {
            bundle.putString("editDailyReport", dailyReportListItem.id.toString())
        }
        addDailyReportToAdmin.arguments = bundle
        (requireActivity() as MainActivity).openOtherFragment(addDailyReportToAdmin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = Preference(requireContext())
        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.toolbarLayout.screenTitle.text = "Daily Report List"
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )
        callGetDailyReportListApi()
    }

    private fun callGetDailyReportListApi() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().getDailyReportFragmentList(preference?.getString(AppConstant.SALESMAN_ID, ""))
            .enqueue(object : Callback<DailyReportListResponse> {
                override fun onResponse(
                    call: Call<DailyReportListResponse>,
                    response: Response<DailyReportListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            binding.dailyReportListRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                            binding.dailyReportListRecyclerview.adapter = DailyReportListFragmentAdapter(body.data, dailyReportListFragmentItemClickListener, requireContext())
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<DailyReportListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }

    override fun onDailyReportFragmentItemClick(dailyReportListItem: DailyReportListItem) {
        openAddDailyReportToAdmin(dailyReportListItem)
    }
}