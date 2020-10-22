package net.aridai.simpleqiitaclient.application

import net.aridai.simpleqiitaclient.application.article.ArticleSearchInteractor
import net.aridai.simpleqiitaclient.application.article.ArticleSearchUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object Application {
    val koinModule: Module = module {
        factory<ArticleSearchUseCase> { ArticleSearchInteractor(queryService = get()) }
    }
}
