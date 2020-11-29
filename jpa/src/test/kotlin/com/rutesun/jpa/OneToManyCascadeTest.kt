package com.rutesun.jpa

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.LinkedList
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "parent")
data class Parent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
) {
    @OneToMany(cascade = [CascadeType.REMOVE, CascadeType.PERSIST],  mappedBy = "parent")
    val children: MutableList<Child> = LinkedList()
}

@Entity
@Table(name = "child")
data class Child(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
) {
    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent: Parent? = null
}

@RunWith(SpringRunner::class)
@DataJpaTest
class OneToManyCascadeTest {


    @Autowired
    private lateinit var em: EntityManager

    @Test
    fun insertTest() {

        val p = Parent()
        p.children.add(Child())
        p.children.add(Child())
        em.persist(p)

//        val list = em.createNativeQuery("select p.* from parent p join child c ON p.id = c.parent_id", Parent::class.java).resultList
        val list = em.createQuery("SELECT p FROM Parent p JOIN FETCH p.children").resultList
        list.forEach {
            println(it)
        }
    }

    @Test
    fun deleteTest() {
        val p = Parent()
        p.children.add(Child())
        p.children.add(Child())
        em.persist(p)

        em.flush()

        println("Saved")

        em.remove(p)

        em.flush()
    }
}