package jp.co.betacomputing.simpleqiitaclient.ui.article

import org.threeten.bp.LocalDateTime

internal data class Article(
    val id: String,
    val url: String,
    val title: String,
    val authorId: String,
    val authorIconUrl: String,
    val createdAt: LocalDateTime,
    val tags: List<String>,
    val likes: Int,
)
