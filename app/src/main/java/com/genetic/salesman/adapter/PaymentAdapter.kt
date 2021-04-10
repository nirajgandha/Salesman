package com.genetic.salesman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.genetic.salesman.R
import com.genetic.salesman.databinding.PaymentScreenRecyclerItemBinding
import com.genetic.salesman.interfaces.PaymentItemClickListener
import com.genetic.salesman.model.PaymentItem
import com.genetic.salesman.utils.GlideApp
import java.util.*

class PaymentAdapter(private var paymentItemArrayList: ArrayList<PaymentItem>, private val paymentItemClickListener: PaymentItemClickListener, private val context: Context) : RecyclerView.Adapter<PaymentAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = PaymentScreenRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(paymentItemArrayList[position]){
                paymentScreenRecyclerItemBinding.orderNo.text = context.resources.getString(R.string.order_number, orderId)
                paymentScreenRecyclerItemBinding.paymentAmount.text = context.resources.getString(R.string.amount_s,
                    totalAmount.toString())
                val splitter = paymentDate.split(" ")[0].split("-")
                paymentScreenRecyclerItemBinding.paymentDate.text = if (paymentDate.split(" ")[0].indexOf("-") == 2) {
                    "${splitter[0]}-${splitter[1]}-${splitter[2]}"
                } else {
                    "${splitter[2]}-${splitter[1]}-${splitter[0]}"
                }
                paymentScreenRecyclerItemBinding.status.text = type
                paymentScreenRecyclerItemBinding.root.setOnClickListener {
                    paymentItemClickListener.onPaymentItemClick(this)
                }
                if (image.isNotEmpty()) {
                    GlideApp.with(context)
                        .load(image)
                        .fitCenter()
                        .into(paymentScreenRecyclerItemBinding.roundImgLayout.roundedImageView)
                        .onLoadFailed(
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.logo,
                                context.theme
                            )
                        )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return paymentItemArrayList.size
    }

    class ViewHolder(val paymentScreenRecyclerItemBinding: PaymentScreenRecyclerItemBinding) : RecyclerView.ViewHolder(paymentScreenRecyclerItemBinding.root)

}