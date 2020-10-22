package net.aridai.simpleqiitaclient.application.article

interface ArticleQueryService {

    //  記事の検索を行う。
    //  (今回は簡略化のため、エラーは単にnullかどうかで表現する。)
    suspend fun search(keyword: String, page: Int, count: Int): List<ArticleSnapshot>?
}
