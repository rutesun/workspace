package com.rutesun.jpa

import java.time.LocalDateTime

class Order(
    val products: List<Product>
) {
    val orderedAt: LocalDateTime = LocalDateTime.now()
}