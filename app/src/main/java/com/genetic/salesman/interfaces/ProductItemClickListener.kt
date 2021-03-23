package com.genetic.salesman.interfaces

import com.genetic.salesman.model.ProductListItem

interface ProductItemClickListener {
    fun onProductItemClick(productListItem: ProductListItem)
}