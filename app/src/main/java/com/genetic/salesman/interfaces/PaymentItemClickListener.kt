package com.genetic.salesman.interfaces

import com.genetic.salesman.model.PaymentItem

interface PaymentItemClickListener {
    fun onPaymentItemClick(paymentItem: PaymentItem)
}