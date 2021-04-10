package com.genetic.salesman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.genetic.salesman.R
import com.genetic.salesman.databinding.OrderScreenRecyclerItemBinding
import com.genetic.salesman.interfaces.OrderItemClickListener
import com.genetic.salesman.model.OrderItem
import java.util.*
import java.util.concurrent.TimeUnit

class OrderAdapter(private var orderItemList: ArrayList<OrderItem>, private val orderItemClickListener: OrderItemClickListener, private val context: Context) : RecyclerView.Adapter<OrderAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = OrderScreenRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(orderItemList[position]){
                orderScreenRecyclerItemBinding.orderTitle.text = context.resources.getString(R.string.order_number, orderId)
                orderScreenRecyclerItemBinding.salesManName.text = salemanName
                if (lrUpload.isEmpty()) {
                    orderScreenRecyclerItemBinding.lrUploaded.text = context.resources.getString(R.string.lr_uploaded_s, "No")
                } else {
                    orderScreenRecyclerItemBinding.lrUploaded.text = context.resources.getString(R.string.lr_uploaded_s, "Yes")
                }
                if (dueDate.isNotEmpty() && dueDate.length == "yyyy-mm-dd".length) {
                    val dateCal = Calendar.getInstance()
                    val splitter = dueDate.split(" ")[0].split("-")
                    orderScreenRecyclerItemBinding.date.text = if (dueDate.split(" ")[0].indexOf("-") == 2) {
                        dateCal.set(Calendar.YEAR, splitter[2].toInt())
                        dateCal.set(Calendar.MONTH, splitter[1].toInt()-1)
                        dateCal.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                        "${splitter[0]}-${splitter[1]}-${splitter[2]}"
                    } else {
                        dateCal.set(Calendar.YEAR, splitter[0].toInt())
                        dateCal.set(Calendar.MONTH, splitter[1].toInt()-1)
                        dateCal.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                        "${splitter[2]}-${splitter[1]}-${splitter[0]}"
                    }

                    val timeDiff = Calendar.getInstance().timeInMillis - dateCal.timeInMillis
                    val days = TimeUnit.MILLISECONDS.toDays(timeDiff)
                    when {
                        days >= 120 -> {
                            orderScreenRecyclerItemBinding.orderBg.background = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_head_red_background, context.theme)
                        }
                        days >= 90 -> {
                            orderScreenRecyclerItemBinding.orderBg.background = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_head_blue_background, context.theme)
                        }
                        days >= 60 -> {
                            orderScreenRecyclerItemBinding.orderBg.background = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_head_green_background, context.theme)
                        }
                        else -> {
                            orderScreenRecyclerItemBinding.orderBg.background = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_head_background, context.theme)
                        }
                    }
                }
                orderScreenRecyclerItemBinding.amount.text = context.resources.getString(R.string.amount_s,
                    totalAmount.toString())
                orderScreenRecyclerItemBinding.status.text = orderStatus
                orderScreenRecyclerItemBinding.root.setOnClickListener {
                    orderItemClickListener.onOrderItemClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    class ViewHolder(val orderScreenRecyclerItemBinding: OrderScreenRecyclerItemBinding) : RecyclerView.ViewHolder(orderScreenRecyclerItemBinding.root)

}