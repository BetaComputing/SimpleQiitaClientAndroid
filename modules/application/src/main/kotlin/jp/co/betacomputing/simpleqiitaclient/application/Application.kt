package jp.co.betacomputing.simpleqiitaclient.application

import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchInteractor
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object Application {
    val koinModule: Module = module {
        factory<ArticleSearchUseCase> { ArticleSearchInteractor(queryService = get()) }
    }
}
