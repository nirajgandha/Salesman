package com.genetic.salesman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class GetDailyReportListFragment : Fragment(), DailyReportListFragmentItemClickListener {
    private var _binding: FragmentDailyReportListBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val dailyReportListFragmentItemClickListener: DailyReportListFragmentItemClickListener = this
    private val CLICK_DRAG_TOLERANCE = 10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private var downRawX: Float = 0f
    private var downRawY: Float = 0f
    private var dX: Float = 0f
    private var dY:Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReportListBinding.inflate(inflater)
        binding.fab.setOnClickListener { openAddDailyReportToAdmin(null) }
        binding.fab.setOnLongClickListener { v ->
            v.setOnTouchListener { view, event ->
                val layoutParams = view.layoutParams as MarginLayoutParams
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        downRawX = event.rawX
                        downRawY = event.rawY
                        dX = view.x - downRawX
                        dY = view.y - downRawY
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val viewWidth = view.width
                        val viewHeight = view.height
                        val viewParent = view.parent as View
                        val parentWidth = viewParent.width
                        val parentHeight = viewParent.height
                        var newX: Float = event.rawX + dX
                        newX = max(
                            layoutParams.leftMargin.toFloat(),
                            newX
                        ) // Don't allow the FAB past the left hand side of the parent

                        newX = min(
                            parentWidth - viewWidth - layoutParams.rightMargin.toFloat(),
                            newX
                        ) // Don't allow the FAB past the right hand side of the parent

                        var newY: Float = event.rawY + dY
                        newY = max(
                            layoutParams.topMargin.toFloat(),
                            newY
                        ) // Don't allow the FAB past the top of the parent

                        newY = min(
                            parentHeight - viewHeight - layoutParams.bottomMargin.toFloat(),
                            newY
                        ) // Don't allow the FAB past the bottom of the parent


                        view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_UP -> {
                        val upRawX: Float = event.rawX
                        val upRawY: Float = event.rawY

                        val upDX = upRawX - downRawX
                        val upDY = upRawY - downRawY

                        if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                            return@setOnTouchListener view.performClick()
                        } else {
                            return@setOnTouchListener true
                        }
                    }
                    else -> {
                        return@setOnTouchListener false
                    }
                }
            }
            return@setOnLongClickListener false
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
        APIClient.getApiInterface().getDailyReportFragmentList(
            preference?.getString(
                AppConstant.SALESMAN_ID,
                ""
            )
        )
            .enqueue(object : Callback<DailyReportListResponse> {
                override fun onResponse(
                    call: Call<DailyReportListResponse>,
                    response: Response<DailyReportListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            val layoutManager = LinearLayoutManager(requireContext())
                            binding.dailyReportListRecyclerview.layoutManager = layoutManager
                            val decorationItem = DividerItemDecoration(
                                requireContext(),
                                layoutManager.orientation
                            )
                            binding.dailyReportListRecyclerview.addItemDecoration(decorationItem)
                            binding.dailyReportListRecyclerview.adapter =
                                DailyReportListFragmentAdapter(
                                    body.data,
                                    dailyReportListFragmentItemClickListener,
                                    requireContext()
                                )
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