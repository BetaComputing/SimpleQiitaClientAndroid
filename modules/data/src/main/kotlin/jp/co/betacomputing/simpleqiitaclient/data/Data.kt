package jp.co.betacomputing.simpleqiitaclient.data

import jp.co.betacomputing.simpleqiitaclient.data.article.ArticleQueryService
import jp.co.betacomputing.simpleqiitaclient.data.article.QiitaApi
import org.koin.core.module.Module
import org.koin.dsl.module
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleQueryService as IArticleQueryService

object Data {
    val koinModule: Module = module {
        single { QiitaApi.create() }
        single<IArticleQueryService> { ArticleQueryService(api = get()) }
    }
}
