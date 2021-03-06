package com.rutesun.transaction

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest(
    properties = [
        "spring.jpa.show-sql=false"
    ]
)
internal class TransactionTest {

    @Autowired
    private lateinit var transactionA: TransactionA

    @Autowired
    private lateinit var transactionWithLock: TransactionWithLock

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
    fun `트랜잭션 롤백 테스트 - rollback`() {
        transactionA.test2(rollback = true)

        val items = em.createQuery("select i from Item i").resultList
        assertEquals(1, items.size)
    }

    @Test
    fun `트랜잭션 롤백 테스트 - no rollback`() {
        transactionA.test2(rollback = false)

        val items = em.createQuery("select i from Item i").resultList
        assertEquals(2, items.size)
    }


}