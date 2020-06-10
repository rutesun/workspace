package com.rutesun.transaction

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest
internal class TransactionTest {

    @Autowired
    private lateinit var transactionA: TransactionA

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @PersistenceContext
    private lateinit var em: EntityManager

    @Before
    fun cleanUp() {
        itemRepository.deleteAll()
    }

    @Test
    fun `트랜잭션 propagation 에 따른 name, isolation level`() {
        transactionA.test1()
        transactionA.test2()
    }

    @Test
    fun `트랜잭션 롤백 테스트`() {
        try {
            transactionA.test2(rollback = true)
        } catch (e: RuntimeException) {
            // pass
        }

        val items = em.createQuery("select i from Item i").resultList
        assertTrue(items.isEmpty())
    }
}