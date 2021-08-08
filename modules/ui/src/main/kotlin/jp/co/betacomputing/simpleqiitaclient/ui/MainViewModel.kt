package jp.co.betacomputing.simpleqiitaclient.ui

import androidx.lifecycle.*
import jp.co.betacomputing.simpleqiitaclient.application.QiitaTagPageProvider
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchRequest
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchResponse
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchUseCase
import jp.co.betacomputing.simpleqiitaclient.common.LiveEvent
import jp.co.betacomputing.simpleqiitaclient.common.MutableLiveEvent
import jp.co.betacomputing.simpleqiitaclient.common.publish
import jp.co.betacomputing.simpleqiitaclient.ui.article.Article
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val articleSearchUseCase: ArticleSearchUseCase,
    private val tagPageProvider: QiitaTagPageProvider,
) : ViewModel() {

    //  取得中かどうか
    private val isFetching: MutableLiveData<Boolean> = MutableLiveData(false)

    //  記事リスト
    //  (検索結果が0件の場合は空リストで表現し、初期状態はnullで表現する。)
    //  (今回は更新処理でリスト全体がまとめて更新されるケースしかないため、リスト自体をLiveDataで持たせることにする。)
    private val _articleList = MutableLiveData<List<Article>?>()
    val articleList: LiveData<List<Article>?> = this._articleList

    //  検索キーワード
    val keyword: MutableLiveData<String> = MutableLiveData("")

    //  検索ボタンが有効かどうか
    //  (取得処理中でない、かつ、検索キーワードが空でないときに有効となる。)
    val isSearchButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().also {
        val onChanged = { it.value = this.isFetching.value == false && !this.keyword.value.isNullOrBlank() }
        it.addSource(this.isFetching) { onChanged() }
        it.addSource(this.keyword) { onChanged() }
    }

    //  プログレスバーを表示するかどうか
    val isProgressBarVisible: LiveData<Boolean> = this.isFetching

    //  初期メッセージを表示するかどうか
    //  (取得中ではなく、かつ、記事リストが未設定の場合)
    val isInitialMessageVisible: LiveData<Boolean> = MediatorLiveData<Boolean>().also {
        val onChanged = {
            it.value = this.isFetching.value == false && this.articleList.value == null
        }
        it.addSource(this.isFetching) { onChanged() }
        it.addSource(this.articleList) { onChanged() }
    }

    //  空メッセージを表示するかどうか
    //  (取得中ではなく、かつ、記事リストが空リストの場合)
    val isEmptyMessageVisible: LiveData<Boolean> = MediatorLiveData<Boolean>().also {
        val onChanged = {
            it.value = this.isFetching.value == false && this.articleList.value?.isEmpty() ?: false
        }
        it.addSource(this.isFetching) { onChanged() }
        it.addSource(this.articleList) { onChanged() }
    }

    //  検索が失敗したことを通知するイベント
    private val _failedEvent = MutableLiveEvent<Unit>()
    val failedEvent: LiveEvent<Unit> = this._failedEvent

    //  キーボードの非表示を要求するイベント
    private val _keyboardHiddenRequestEvent = MutableLiveEvent<Unit>()
    val keyboardHiddenRequestEvent: LiveEvent<Unit> = this._keyboardHiddenRequestEvent

    //  Qiita記事への遷移を要求するイベント
    //  (パラメタとしてURL文字列を渡す。)
    private val _toArticlePageRequest = MutableLiveEvent<String>()
    val toArticlePageRequest: LiveEvent<String> = this._toArticlePageRequest

    //  Qiitaのタグページへの遷移を要求するイベント
    //  (パラメタとしてURL文字列を渡す。)
    private val _toTagPageRequest = MutableLiveEvent<String>()
    val toTagPageRequest: LiveEvent<String> = this._toTagPageRequest

    //  検索ボタンがクリックされたとき。
    fun onSearchButtonClicked() {
        //  キーボードの非表示要求を投げる。
        this._keyboardHiddenRequestEvent.publish(Unit)

        //  検索ボタンが無効時に押されても検索処理は行わない。
        //  (キーボードのEnterキーが押されたときもこのコールバックが呼ばれるため、それを弾いている。)
        if (this.isSearchButtonEnabled.value != true) return

        //  入力された検索キーワードを元に検索処理を走らせる。
        val keyword = this.keyword.value!!
        this.viewModelScope.launch { this@MainViewModel.search(keyword) }
    }

    //  記事がクリックされたとき。
    fun onArticleClicked(article: Article) {
        val url = article.url
        this._toArticlePageRequest.publish(url)
    }

    //  タグがクリックされたとき。
    fun onTagClicked(tag: String) {
        val url = this.tagPageProvider.toQiitaTagPageUrl(tag)
        this._toTagPageRequest.publish(url)
    }

    //  検索を行う。
    private suspend fun search(keyword: String) {
        this.isFetching.value = true

        val request = ArticleSearchRequest(keyword)
        when (val response = this.articleSearchUseCase.execute(request)) {
            //  成功時
            is ArticleSearchResponse.Success -> {
                val newArticleList = response.articles.map(ArticleDtoConverter::convert)
                this._articleList.value = newArticleList
            }

            //  失敗時
            is ArticleSearchResponse.Failure -> {
                this._articleList.value = null
                this._failedEvent.publish(Unit)
            }
        }

        this.isFetching.value = false
    }
}
