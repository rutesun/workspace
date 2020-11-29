package com.rutesun.jpa

import java.time.LocalDate
import java.time.LocalDateTime

enum class ShipStatus {
    CANCELED,
    PENDING,
    SHIPPING,
    COMPLETED
}


class Ship(
    val address: Address,
    val order: Order
) {
    var status: ShipStatus = ShipStatus.PENDING
        private set
    var shippedAt: LocalDateTime? = null
        private set
}