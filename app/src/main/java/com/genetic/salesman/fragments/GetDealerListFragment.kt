package com.genetic.salesman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class GetDealerListFragment : Fragment(), DealerListFragmentItemClickListener {
    private var _binding: FragmentDealerListBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val dealerListFragmentItemClickListener: DealerListFragmentItemClickListener = this
    private val CLICK_DRAG_TOLERANCE = 10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private var downRawX: Float = 0f
    private var downRawY: Float = 0f
    private var dX: Float = 0f
    private var dY:Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealerListBinding.inflate(inflater)
        binding.fab.setOnClickListener { openAddDealerFragment(null) }
        binding.fab.setOnLongClickListener { v ->
            v.setOnTouchListener { view, event ->
                val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
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
                            val layoutManager = LinearLayoutManager(requireContext())
                            binding.dealerListRecyclerview.layoutManager = layoutManager
                            val decorationItem = DividerItemDecoration(requireContext(), layoutManager.orientation)
                            binding.dealerListRecyclerview.addItemDecoration(decorationItem)
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