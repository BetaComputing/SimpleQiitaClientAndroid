package net.aridai.simpleqiitaclient.ui

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.aridai.simpleqiitaclient.ui.article.Article
import timber.log.Timber

internal class MainViewModel : ViewModel() {

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
        //  TODO: 検索処理
        Timber.d("検索: $keyword")
        delay(1000L)

        val dummyList = List(size = 20) { Article.DUMMY }
        this._articleList.value = dummyList

        this.isFetching.value = false
    }
}
