package com.genetic.salesman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.genetic.salesman.databinding.DealerListRecyclerItemBinding
import com.genetic.salesman.interfaces.DealerListFragmentItemClickListener
import com.genetic.salesman.model.DealerFragmentItem
import java.util.*

class DealerListFragmentAdapter(private var dealerFragmentArrayList: ArrayList<DealerFragmentItem>, private val dealerListFragmentItemClickListener: DealerListFragmentItemClickListener, private val context: Context) : RecyclerView.Adapter<DealerListFragmentAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = DealerListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(dealerFragmentArrayList[position]){
                dealerListRecyclerItemBinding.firmName.text = firmName
                dealerListRecyclerItemBinding.responsiblePersonName.text = responsiblePersonName
                dealerListRecyclerItemBinding.mobileNo.text = mobileno.toString()
                dealerListRecyclerItemBinding.email.visibility = View.GONE
                dealerListRecyclerItemBinding.icEdit.visibility = View.GONE
                dealerListRecyclerItemBinding.root.setOnClickListener { dealerListFragmentItemClickListener.onDealerFragmentItemClick(this) }
            }
        }
    }

    override fun getItemCount(): Int {
        return dealerFragmentArrayList.size
    }

    class ViewHolder(val dealerListRecyclerItemBinding: DealerListRecyclerItemBinding) : RecyclerView.ViewHolder(dealerListRecyclerItemBinding.root)

}