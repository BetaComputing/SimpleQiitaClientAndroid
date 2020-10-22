package net.aridai.simpleqiitaclient.ui

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.aridai.simpleqiitaclient.application.article.ArticleSearchRequest
import net.aridai.simpleqiitaclient.application.article.ArticleSearchResponse
import net.aridai.simpleqiitaclient.application.article.ArticleSearchUseCase
import net.aridai.simpleqiitaclient.ui.article.Article

internal class MainViewModel(
    private val articleSearchUseCase: ArticleSearchUseCase
) : ViewModel() {

    //  取得中かどうか
    private val isFetching: MutableLiveData<Boolean> = MutableLiveData(false)

    //  記事リスト
    //  (今回は更新処理でリスト全体がまとめて更新されるケースしかないため、リスト自体をLiveDataで持たせることにする。)
    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = this._articleList

    //  検索キーワード
    val keyword: MutableLiveData<String> = MutableLiveData("")

    //  検索ボタンが有効かどうか
    //  (取得処理中でない、かつ、検索キーワードが空でないときに有効となる。)
    val isSearchButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().also {
        val onChanged = { it.value = this.isFetching.value == false && !this.keyword.value.isNullOrEmpty() }
        it.addSource(this.isFetching) { onChanged() }
        it.addSource(this.keyword) { onChanged() }
    }

    //  プログレスバーを表示するかどうか
    val isProgressBarVisible: LiveData<Boolean> = this.isFetching

    //  検索ボタンがクリックされたとき。
    fun onSearchButtonClicked() {
        this.isFetching.value = true

        this.let { vm ->
            vm.viewModelScope.launch { vm.search(keyword = vm.keyword.value!!) }
        }
    }

    //  検索を行う。
    private suspend fun search(keyword: String) {
        val request = ArticleSearchRequest(keyword)

        when (val response = this.articleSearchUseCase.execute(request)) {
            is ArticleSearchResponse.Success -> {
                val newArticleList = response.articles.map(ArticleDtoConverter::convert)
                this._articleList.value = newArticleList
            }

            is ArticleSearchResponse.Failure -> {
                //  TODO: Snackbarでも表示するか?
            }
        }

        this.isFetching.value = false
    }
}
