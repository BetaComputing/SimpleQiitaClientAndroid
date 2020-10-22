package net.aridai.simpleqiitaclient.application.article

import org.threeten.bp.LocalDateTime

data class ArticleSnapshot(
    val id: String,
    val url: String,
    val title: String,
    val authorId: String,
    val authorIconUrl: String,
    val createdAt: LocalDateTime,
    val tags: List<String>,
    val likes: Int,
)
