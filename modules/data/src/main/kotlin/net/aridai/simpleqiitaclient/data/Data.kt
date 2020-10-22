package net.aridai.simpleqiitaclient.data

import net.aridai.simpleqiitaclient.data.article.ArticleQueryService
import net.aridai.simpleqiitaclient.data.article.QiitaApi
import org.koin.core.module.Module
import org.koin.dsl.module
import net.aridai.simpleqiitaclient.application.article.ArticleQueryService as IArticleQueryService

object Data {
    val koinModule: Module = module {
        single { QiitaApi.create() }
        single<IArticleQueryService> { ArticleQueryService(api = get()) }
    }
}
