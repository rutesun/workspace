package com.rutesun.transaction

import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.lang.RuntimeException
import javax.persistence.EntityManager

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
class TransactionA(
    private val em: EntityManager,
    private val transactionB: TransactionB
) {

    fun test1() {
        println("Start transaction 1")
        println("Outer transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Outer transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
        transactionB.test1()
        println("End transaction 1")
    }

    fun test2(rollback: Boolean = false) {
        println("Start transaction 2")
        println("Outer transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Outer transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
        em.persist(Item(amount = 1000, name = "Item-Outer"))
        transactionB.test2(rollback)
        println("End transaction 2")
    }
}

@Service
class TransactionB(
    private val em: EntityManager
){

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun test1() {
        println("Inner transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Inner transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    fun test2(rollback: Boolean) {
        val session = em.unwrap(Session::class.java)
        session.transaction
        println("Inner transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Inner transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
        em.persist(Item(amount = 1000, name = "Item-Inner"))

        if (rollback) throw RuntimeException("throw exception for rollback")
    }
}