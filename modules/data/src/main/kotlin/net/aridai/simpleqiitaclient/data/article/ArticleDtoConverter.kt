package net.aridai.simpleqiitaclient.data.article

import net.aridai.simpleqiitaclient.application.article.ArticleSnapshot
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

internal object ArticleDtoConverter {

    fun convert(source: ArticleDto): ArticleSnapshot =
        ArticleSnapshot(
            id = source.id,
            url = source.url,
            title = source.title,
            authorId = source.user.id,
            authorIconUrl = source.user.profileImageUrl,
            createdAt = LocalDateTime.parse(source.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            tags = source.tags.map { it.name },
            likes = source.likesCount,
        )
}
