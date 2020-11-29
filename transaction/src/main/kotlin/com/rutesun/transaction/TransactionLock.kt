package com.rutesun.transaction

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
internal class TransactionWithLock(
    private val em: EntityManager,
    private val inner: TransactionWithLockInner,
    private val itemRepository: ItemRepository) {

    fun selectItem(itemId: Long) = itemRepository.findById(itemId).get()


    fun updateItem(itemId: Long) {
        inner.updateItem(itemId)
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("\n=== When inner tx is finished, Tx list in Outer ===")
        resultList.forEach(::println)
    }

    fun updateItemWithNewTx(itemId: Long) {
        inner.updateItemInNewTx(itemId)
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("\n=== When inner tx is finished, Tx list in Outer ===")
        resultList.forEach(::println)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateItemWithNestedTx(itemId: Long) {
        inner.updateItemInNestedTx(itemId)
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("\n=== When inner tx is finished, Tx list in Outer ===")
        resultList.forEach(::println)
    }
}

@Service
internal class TransactionWithLockInner(
    private val em: EntityManager,
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun updateItem(itemId: Long) {
        val item = itemRepository.findWithLockById(itemId)!!
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("=== Before tx list in inner ===")
        resultList.forEach(::println)
        item.amount *= 2
        itemRepository.save(item)
        println("=== After Update in inner ===")
        resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        resultList.forEach(::println)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateItemInNewTx(itemId: Long) {
        val item = itemRepository.findWithLockById(itemId)!!
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("=== Before Update in inner ===")
        resultList.forEach(::println)
        item.amount *= 2
        itemRepository.save(item)
        println("=== After Update in inner ===")
        resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        resultList.forEach(::println)
    }

    @Transactional(propagation = Propagation.NESTED)
    fun updateItemInNestedTx(itemId: Long) {
        val item = itemRepository.findWithLockById(itemId)!!
        var resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        println("=== Before Update in inner ===")
        resultList.forEach(::println)
        item.amount *= 2
        itemRepository.save(item)
        println("=== After Update in inner ===")
        resultList = em.createNativeQuery("SELECT * FROM information_schema.innodb_trx").resultList
        resultList.forEach(::println)
    }
}