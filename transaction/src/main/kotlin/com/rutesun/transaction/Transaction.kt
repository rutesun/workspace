package com.rutesun.transaction

import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.transaction.support.TransactionSynchronizationUtils
import java.lang.RuntimeException
import javax.persistence.EntityManager

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
internal class TransactionA (
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
        val session = em.unwrap(Session::class.java)
        val tx = session.transaction
        println("Tx: ${tx}\t${tx.hashCode()}")
        println("Start transaction 2")
        println("Outer transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Outer transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
        em.persist(Item(amount = 1000, name = "Item-Outer")).also { em.flush() }
        try {
            transactionB.test2(rollback)
        } catch (e: RuntimeException) { }
        println("Outer transaction status = ${tx.status}")
        println("End transaction 2")
    }
}

@Service
internal class TransactionB(
    private val em: EntityManager
){

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun test1() {
        println("Inner transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Inner transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    fun test2(rollback: Boolean = false) {
        val session = em.unwrap(Session::class.java)
        val tx = session.transaction
        println("Tx: ${tx.toString()}\t${tx.hashCode()}")
        println("Inner transaction name = ${TransactionSynchronizationManager.getCurrentTransactionName()}")
        println("Inner transaction isolation level = ${TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()}")
        em.persist(Item(amount = 1000, name = "Item-Inner")).also { em.flush() }

        if (rollback) {
            throw RuntimeException()
        }
    }
}