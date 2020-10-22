package net.aridai.simpleqiitaclient.application

import kotlinx.coroutines.delay
import net.aridai.simpleqiitaclient.application.article.ArticleQueryService
import net.aridai.simpleqiitaclient.application.article.ArticleSearchInteractor
import net.aridai.simpleqiitaclient.application.article.ArticleSearchUseCase
import net.aridai.simpleqiitaclient.application.article.ArticleSnapshot
import org.koin.core.module.Module
import org.koin.dsl.module
import org.threeten.bp.LocalDateTime
import kotlin.random.Random

object Application {
    val koinModule: Module = module {
        factory<ArticleSearchUseCase> { ArticleSearchInteractor(queryService = get()) }

        single<ArticleQueryService> {
            object : ArticleQueryService {
                override suspend fun search(keyword: String, page: Int, count: Int): List<ArticleSnapshot>? {
                    delay(1000L)

                    return when (Random.nextInt() % 3) {
                        0 -> null
                        else -> List(size = 20) { i ->
                            ArticleSnapshot(
                                id = "ID_$i",
                                url = "https://example.com/articles/$i",
                                title = "記事$i",
                                authorId = "user_$i",
                                authorIconUrl = "http://placehold.jp/128x128.png?text=user_$i",
                                createdAt = LocalDateTime.now().minusMinutes(i.toLong()),
                                tags = listOf("Android", "iOS", "Kotlin"),
                                likes = i + 10,
                            )
                        }
                    }
                }
            }
        }
    }
}
