package com.genetic.salesman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.databinding.FragmentThankyouBinding


class ThankYouFragment: Fragment(){
    private var _binding: FragmentThankyouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThankyouBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.homeTv.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.toolbarLayout.screenTitle.text = resources.getString(R.string.order_number, requireArguments().getString("orderId"))
        binding.totalOrderAmount.text = resources.getString(R.string.amount_s, requireArguments().getString("amount"))
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )

    }
}