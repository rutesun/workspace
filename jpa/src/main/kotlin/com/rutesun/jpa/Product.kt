package com.rutesun.jpa

enum class ProductStatus {
    PENDING,
    ON_SALE,
    SOLD_OUT
}

class Product(
    val name: String,
    val price: Int
) {
    var status: ProductStatus = ProductStatus.PENDING
        private set
}