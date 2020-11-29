package com.rutesun.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import javax.persistence.LockModeType

interface ItemRepository: JpaRepository<Item, Number> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: Long): Item?
}