package jp.co.betacomputing.simpleqiitaclient.application.article

//  記事を検索するユースケース
interface ArticleSearchUseCase {
    suspend fun execute(request: ArticleSearchRequest): ArticleSearchResponse
}

data class ArticleSearchRequest(val keyword: String)

sealed class ArticleSearchResponse {

    //  取得成功時
    data class Success(val articles: List<ArticleSnapshot>) : ArticleSearchResponse()

    //  取得失敗時
    //  (今回は簡略化のため、細かな区別 (ネット環境エラー・サーバ側のエラー・キャンセル等) は行わない。
    object Failure : ArticleSearchResponse()
}
