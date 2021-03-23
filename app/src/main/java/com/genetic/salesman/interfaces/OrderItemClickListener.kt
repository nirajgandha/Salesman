package com.genetic.salesman.interfaces

import com.genetic.salesman.model.OrderItem

interface OrderItemClickListener {
    fun onOrderItemClick(orderItem: OrderItem)
}