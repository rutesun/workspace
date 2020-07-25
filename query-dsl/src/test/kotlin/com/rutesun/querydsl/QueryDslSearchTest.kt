package com.rutesun.querydsl

import com.querydsl.core.QueryResults
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.querydsl.QPageRequest
import org.springframework.data.querydsl.QSort
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

fun wrapEmptyLine(message: String? = null, lambda: () -> Unit) {
    println("\nStart: $message".takeIf { !message.isNullOrEmpty() } ?: "")
    lambda()
    println("End: $message\n".takeIf { !message.isNullOrEmpty() } ?: "")
}

inline fun <reified T> QueryResults<T>.toPageImpl(): Page<T> {
    return org.springframework.data.domain.PageImpl(this.results, PageRequest.of((this.offset / this.limit).toInt(), this.limit.toInt()), this.total)
}

@RunWith(SpringRunner::class)
@DataJpaTest
class QueryDslSearchTest {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var testEm: TestEntityManager

    @Autowired
    private lateinit var postRepository: PostRepository

    private val factory: JPAQueryFactory by lazy {
        JPAQueryFactory(em)
    }

    private var posts: List<Post> = emptyList()

    @Before
    fun prepare() {
        val tagTIL = Tag("TIL").also { em.persist(it) }
        val tagMysql = Tag("mysql").also { em.persist(it) }
        val tagDevday = Tag("devday").also { em.persist(it) }

        posts = listOf(Post("Post 1", tags = setOf(tagDevday, tagTIL)),
            Post("Post 2", tags = setOf(tagMysql, tagTIL)).apply { status = PostStatus.PUBLISH })
        postRepository.saveAll(posts)
        em.flush()
    }

    @Test
    fun `PredicateExecutor 를 이용한 쿼리`() {
        val qPost = QPost.post

        var results = postRepository.findAll()

        wrapEmptyLine { results.forEach(::println) }

        results = postRepository.findAll(qPost.status.eq(PostStatus.PUBLISH)).toList()
        wrapEmptyLine { results.forEach(::println) }

        results = postRepository.findAll(qPost.title.desc()).toList()
        assertEquals(posts.last().title, results.firstOrNull()?.title)
    }

    @Test
    fun `JPAQueryFactory 를 이용한 쿼리`() {
        val qPost = QPost.post
        var results = factory.selectFrom(qPost).fetch()

        assertEquals(posts.size, results.size)

        results = factory.selectFrom(qPost).where(qPost.status.eq(PostStatus.PUBLISH)).fetch()
        assertNull(results.find { it.status != PostStatus.PUBLISH })
        results = factory.selectFrom(qPost).orderBy(qPost.title.desc()).fetch()
        assertEquals(posts.last().title, results.firstOrNull()?.title)

        val partials = factory.select(qPost.title, qPost.status).from(qPost).where(qPost.status.eq(PostStatus.PUBLISH)).fetch()
        wrapEmptyLine("일부 필드만 조회") { partials.forEach(::println) }
        assertEquals(2, partials.first().size())
    }

    @Test
    fun `Paging`() {
        postRepository.deleteAll()
        val posts = (1..100).mapIndexed { idx, i ->
            if (idx % 2 == 0) Post("Post $i").apply { status = PostStatus.PUBLISH }
            else Post("Post $i")
        }
        postRepository.saveAll(posts)

        val qPost = QPost.post

        // paging
        val page = postRepository.findAll(qPost.status.isNotNull, QPageRequest.of(2, 10, QSort.by(qPost.title.desc())))
        assertTrue(page.totalPages > 1)

        var query = factory.selectFrom(qPost).where(qPost.status.isNotNull)
        var results = query.fetchResults()

        assertEquals(100, query.fetchCount())
        assertEquals(query.fetchCount(), results.total)

        query = factory.selectFrom(qPost).where(qPost.status.isNotNull).limit(10).offset(20).orderBy(qPost.title.desc())
        results = query.fetchResults()

        assertEquals(100, query.fetchCount())
        assertEquals(query.fetchCount(), results.total)

        with(results.toPageImpl()) {
            kotlin.test.assertEquals(this.totalPages, page.totalPages)
            kotlin.test.assertEquals(this.totalElements, page.totalElements)
            kotlin.test.assertEquals(this.pageable.offset, page.pageable.offset)
            kotlin.test.assertEquals(this.content.first(), page.content.first())
        }
    }

    @Test
    fun `연관관계 join`() {
        em.clear()
        val qPost = QPost.post
        // join table 에 대한 alias 를 정해준다
        val qTag = QTag("tags")

        // fetch 만 할 경우 N + 1 문제가 발생한다.
        var list = factory.selectFrom(qPost)
            .innerJoin(qPost.tags, qTag).fetch()

        println("\nN + 1 이 발생!")

        list.map { println(it) }

        with(list.first()) {
            kotlin.test.assertTrue(this.tags.isNotEmpty())
            kotlin.test.assertTrue(this.tags.map { it.label }.contains("devday"))
        }

        // fetchJoin 을 할 경우 같이 조회하기 때문에 N + 1이 발생하지 않음
        // distinct 를 해줘야한다.
        println("\nN + 1 이 발생하지 않는다.")
        list = factory.selectFrom(qPost).distinct()
            .innerJoin(qPost.tags, qTag).fetchJoin().fetch()
        list.map { println(it) }


        println("\nJoin with condition")
        list = factory.selectFrom(qPost)
            .innerJoin(qPost.tags, qTag).where(qTag.label.eq("TIL")).fetch()

        assertEquals(2, list.size)

        // where 절이나 on 절이나 다 사용가능
        list = factory.selectFrom(qPost)
            .innerJoin(qPost.tags, qTag).on(qTag.label.eq("devday")).fetch()
        assertEquals(1, list.size)

        val partials = factory.select(qPost.title, qPost.status, qTag.label).from(qPost).innerJoin(qPost.tags, qTag).fetch()
        wrapEmptyLine("일부 필드만 조회") { partials.forEach(::println) }
    }

    @Test
    fun `연관 관계가 없는 join`() {
        em.persist(Comment("Post1 - Comment1", postId = posts[0].id))
        em.persist(Comment("Post1 - Comment2", postId = posts[0].id))

        val qPost = QPost.post
        val qComment = QComment.comment
        var results = factory.selectFrom(qPost).leftJoin(qComment).on(qPost.id.eq(qComment.postId)).fetch()
        assertEquals(3, results.size)

        results = factory.selectFrom(qPost).innerJoin(qComment).on(qPost.id.eq(qComment.postId)).fetch()
        assertEquals(2, results.size)

        val partials = factory.select(qPost.title, qComment.text, qComment.isSecret).from(qPost).innerJoin(qComment).on(qPost.id.eq(qComment.postId)).fetch()
        wrapEmptyLine("일부 필드만 조회") { partials.forEach(::println) }
    }

    @Test
    fun `Subquery`() {
        em.persist(Comment("Post1 - Comment1", postId = posts[0].id))
        em.persist(Comment("Post1 - Comment2", postId = posts[0].id))

        val qPost = QPost.post
        val qComment = QComment.comment

        var results = factory.selectFrom(qPost).where(qPost.id.`in`(JPAExpressions.select(qComment.postId).from(qComment))).fetch()

        assertEquals(1, results.size)
    }

    data class PostComments(
        val title: String,
        val comments: List<String>
    )

    data class PostDto(
        val title: String,
        val description: String?,
        val commentsCnt: Long = 0
    )

    @Test
    fun `검색 결과를 dto로 변경`() {
        em.persist(Comment("Post1 - Comment1", postId = posts[0].id))
        em.persist(Comment("Post1 - Comment2", postId = posts[0].id))

        val qPost = QPost.post
        val qComment = QComment.comment

        var results = factory.select(Projections.constructor(PostDto::class.java, qPost.title, qPost.description, qComment.count())).from(qPost)
            .leftJoin(qComment).on(qPost.id.eq(qComment.postId)).groupBy(qPost.id).fetch()

        wrapEmptyLine("Dto 로 변환") { results.forEach(::println) }

        assertEquals(2, results[0].commentsCnt)
        assertEquals(0, results[1].commentsCnt)

        var results2 = factory.from(qPost).leftJoin(qComment).on(qPost.id.eq(qComment.postId))
            .transform(groupBy(qPost.id).`as`(Projections.constructor(PostComments::class.java, qPost.title, list(qComment.text))))

        wrapEmptyLine { results2.forEach { (k, v) -> "Post id: $k, $v" } }
    }
}