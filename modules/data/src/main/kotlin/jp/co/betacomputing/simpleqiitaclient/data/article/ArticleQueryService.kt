package jp.co.betacomputing.simpleqiitaclient.data.article

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleQueryService
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSnapshot
import timber.log.Timber
import java.net.URLEncoder

internal class ArticleQueryService(private val api: QiitaApi) : ArticleQueryService {

    override suspend fun search(keyword: String, page: Int, count: Int): List<ArticleSnapshot>? = runCatching {
        //  QiitaのAPIを叩いて変換して返す。
        //  (今回は簡略化のため、細かなエラーハンドリングをせずに適当にrunCatching{}で処理しちゃう。)
        this.api.search(query = "title:${this.encode(keyword)}", perPage = count, page = page)
    }.mapCatching { it.map(ArticleDtoConverter::convert) }.onFailure { Timber.d(it) }.getOrNull()

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun encode(source: String): String = withContext(Dispatchers.IO) {
        URLEncoder.encode(source, Charsets.UTF_8.name())
    }
}
