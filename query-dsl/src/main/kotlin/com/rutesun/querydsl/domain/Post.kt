package com.rutesun.querydsl.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

enum class PostStatus {
    DRAFT,
    PUBLISH
}

@Entity
data class Post(
    var title: String,
    var description: String? = null,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "tags_to_posts",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: Set<Tag> = LinkedHashSet(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {
    @Enumerated(EnumType.STRING)
    var status: PostStatus = PostStatus.DRAFT

    fun addTag(tag: Tag) {
        (this.tags as LinkedHashSet).add(tag)
    }

    override fun toString(): String {
        return "Post(title='$title', description=$description, tags=$tags, id=$id, status=$status)"
    }

//    fun addComment(comment: Comment) {
//        (comments as LinkedList).add(comment)
//    }
//
//    fun removeComment(comment: Comment) {
//        (comments as LinkedList).remove(comment)
//    }
}

interface PostRepository : JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post>
