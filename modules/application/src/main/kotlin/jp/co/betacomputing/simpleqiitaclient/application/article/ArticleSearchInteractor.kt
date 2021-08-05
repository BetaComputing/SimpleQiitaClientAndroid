package jp.co.betacomputing.simpleqiitaclient.application.article

internal class ArticleSearchInteractor(
    private val queryService: ArticleQueryService
) : ArticleSearchUseCase {
    override suspend fun execute(request: ArticleSearchRequest): ArticleSearchResponse {
        val result = this.queryService.search(keyword = request.keyword, page = 1, count = 20)

        return if (result != null) ArticleSearchResponse.Success(result)
        else ArticleSearchResponse.Failure
    }
}
