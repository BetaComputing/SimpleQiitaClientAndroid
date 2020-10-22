package net.aridai.simpleqiitaclient.ui

import net.aridai.simpleqiitaclient.application.article.ArticleSnapshot
import net.aridai.simpleqiitaclient.ui.article.Article

internal object ArticleDtoConverter {

    fun convert(source: ArticleSnapshot): Article =
        Article(
            id = source.id,
            url = source.url,
            title = source.title,
            authorId = source.authorId,
            authorIconUrl = source.authorIconUrl,
            createdAt = source.createdAt,
            tags = source.tags,
            likes = source.likes,
        )
}
