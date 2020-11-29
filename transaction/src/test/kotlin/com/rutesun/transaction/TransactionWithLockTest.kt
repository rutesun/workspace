package com.rutesun.transaction

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RunWith(SpringRunner::class)
@SpringBootTest
class TransactionWithLockTest {
    @Autowired
    private lateinit var transactionA: TransactionA

    @Autowired
    private lateinit var transactionWithLock: TransactionWithLock

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @PersistenceContext
    private lateinit var em: EntityManager

    private lateinit var item: Item
    @Before
    fun cleanUp() {
        itemRepository.deleteAll()
        item = itemRepository.save(Item(amount = 1000, name = "스크류바"))
        itemRepository.flush()
    }

    @Test
    fun `Tx propagation 에 따른 lock release`() {
        transactionWithLock.updateItem(item.id)

        var item = transactionWithLock.selectItem(item.id)
        println(item)
    }

    @Test
    fun `Tx propagation 에 따른 lock release2`() {
        transactionWithLock.updateItemWithNewTx(item.id)

        var item = transactionWithLock.selectItem(item.id)
        println(item)
    }

    @Test
    fun `Tx propagation 에 따른 lock release3`() {

        transactionWithLock.updateItemWithNestedTx(item.id)

        var item = transactionWithLock.selectItem(item.id)
        println(item)
    }
}