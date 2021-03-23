package com.genetic.salesman.interfaces

import com.genetic.salesman.model.ProductOption

interface ProductOptionListener {
    fun onProductOptionClick(productOption: ProductOption)
}