package com.genetic.salesman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genetic.salesman.R
import com.genetic.salesman.databinding.DailyReportRecyclerItemBinding
import com.genetic.salesman.databinding.DealerListRecyclerItemBinding
import com.genetic.salesman.interfaces.DailyReportListFragmentItemClickListener
import com.genetic.salesman.interfaces.DealerListFragmentItemClickListener
import com.genetic.salesman.model.DailyReportListItem
import com.genetic.salesman.model.DealerFragmentItem
import com.genetic.salesman.utils.GlideApp
import java.util.*

class DailyReportListFragmentAdapter(
    private var dailyReportArrayList: ArrayList<DailyReportListItem>,
    private val dailyReportListFragmentItemClickListener: DailyReportListFragmentItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<DailyReportListFragmentAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = DailyReportRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(dailyReportArrayList[position]) {
                dailyReportRecyclerItemBinding.date.text = date
                dailyReportRecyclerItemBinding.dealerName.text = dealerName
                dailyReportRecyclerItemBinding.startKm.text = "Start Km: ${startKm.toString()}"
                dailyReportRecyclerItemBinding.endKm.text = "End Km: ${endKm.toString()}"
                dailyReportRecyclerItemBinding.icEdit.visibility = View.GONE
                dailyReportRecyclerItemBinding.root.setOnClickListener {
                    dailyReportListFragmentItemClickListener.onDailyReportFragmentItemClick(
                        this
                    )
                }
                GlideApp.with(context).load(farmerDelearSelfyPic)
                    .into(dailyReportRecyclerItemBinding.image).onLoadFailed(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.logo,
                            context.theme
                        )
                    )
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyReportArrayList.size
    }

    class ViewHolder(val dailyReportRecyclerItemBinding: DailyReportRecyclerItemBinding) :
        RecyclerView.ViewHolder(dailyReportRecyclerItemBinding.root)

}