package net.aridai.simpleqiitaclient.ui

import android.os.Build
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import net.aridai.simpleqiitaclient.application.article.ArticleSearchResponse
import net.aridai.simpleqiitaclient.application.article.ArticleSearchUseCase
import net.aridai.simpleqiitaclient.application.article.ArticleSnapshot
import net.aridai.simpleqiitaclient.common.observeEventForever
import net.aridai.simpleqiitaclient.ui.article.Article
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.threeten.bp.LocalDateTime

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class MainViewModelTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var mockArticleSearchUseCase: ArticleSearchUseCase

    private val mockArticleListObserver: Observer<List<Article>> = mockk(relaxed = true)
    private val mockKeywordObserver: Observer<String> = mockk(relaxed = true)
    private val mockIsSearchButtonEnabledObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockIsProgressBarVisibleObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockFailedEventObserver: Observer<Unit> = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)

        createMockUseCases()
        createViewModel()
        observeProperties()
    }

    @After
    fun teardown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun 検索キーワードが1文字以上かつ処理中でないときに検索ボタンが有効となる() {
        val delayInMs = 1000L
        coEvery { mockArticleSearchUseCase.execute(any()) } coAnswers {
            delay(delayInMs)
            ArticleSearchResponse.Success(listOf(createDummyArticleSnapshot()))
        }

        //  最初は検索ボタンが無効なはず。
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  検索キーワードを1文字でも入力すると検索ボタンが有効になるはず。
        viewModel.keyword.value = "あ"
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)

        //  1文字以上でも当然有効になるはず。
        viewModel.keyword.value = "あいうえお"
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)

        //  検索を走らせてみる。
        viewModel.onSearchButtonClicked()

        //  すると処理中となって検索ボタンが無効になるはず。
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  文字入力を行っても変わらないはず。
        viewModel.keyword.value = "あい"
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  処理を終わらせてみる。
        testCoroutineDispatcher.advanceTimeBy(delayInMs)

        //  すると再び検索ボタンが有効になるはず。
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)

        //  そして検索キーワードを消すと無効になるはず。
        viewModel.keyword.value = ""
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)
    }

    @Test
    fun 検索に成功すると記事リストの変更通知が飛んでくる() {
        //  IDが「DUMMY_ID_〇〇」であるようなダミーの検索結果の記事リストを返すように設定する。
        val dummy = createDummyArticleSnapshot()
        val list = listOf(
            dummy.copy(id = "DUMMY_ID_1"),
            dummy.copy(id = "DUMMY_ID_2"),
            dummy.copy(id = "DUMMY_ID_3"),
        )
        coEvery { mockArticleSearchUseCase.execute(any()) } returns ArticleSearchResponse.Success(list)

        //  対象の記事リストが設定したダミーのリストと同一のものかどうかを判定するmatcher
        val areTheSame = { it: List<Article> ->
            it.size == 3 && it[0].id == list[0].id && it[1].id == list[1].id && it[2].id == list[2].id
        }

        //  まだこの時点で記事リストの変更通知は飛んできていないはず。
        verify(exactly = 0) { mockArticleListObserver.onChanged(match(areTheSame)) }

        //  検索キーワードを入力して検索を行う。
        viewModel.keyword.value = "KEYWORD"
        viewModel.onSearchButtonClicked()

        //  すると設定した記事リストの変更通知が飛んでくるはず。
        verify { mockArticleListObserver.onChanged(match(areTheSame)) }
        verify(exactly = 0) { mockFailedEventObserver.onChanged(any()) }
    }

    @Test
    fun 検索に失敗すると失敗イベントの通知が飛んでくる() {
        //  検索処理が失敗するように設定する。
        coEvery { mockArticleSearchUseCase.execute(any()) } returns ArticleSearchResponse.Failure

        //  まだこの時点で失敗通知は飛んできていないはず。
        verify(exactly = 0) { mockFailedEventObserver.onChanged(any()) }

        //  検索キーワードを入力して検索を行う。
        viewModel.keyword.value = "KEYWORD"
        viewModel.onSearchButtonClicked()

        //  すると失敗通知が飛んでくるはず。
        verify { mockFailedEventObserver.onChanged(any()) }
    }

    private fun createMockUseCases() {
        mockArticleSearchUseCase = mockk {
            coEvery { execute(any()) } returns ArticleSearchResponse.Success(listOf(createDummyArticleSnapshot()))
        }
    }

    private fun createViewModel() {
        viewModel = MainViewModel(mockArticleSearchUseCase)
    }

    private fun observeProperties() {
        viewModel.articleList.observeForever(mockArticleListObserver)
        viewModel.keyword.observeForever(mockKeywordObserver)
        viewModel.isSearchButtonEnabled.observeForever(mockIsSearchButtonEnabledObserver)
        viewModel.isProgressBarVisible.observeForever(mockIsProgressBarVisibleObserver)
        viewModel.failedEvent.observeEventForever(mockFailedEventObserver)
    }

    private fun createDummyArticleSnapshot(): ArticleSnapshot =
        ArticleSnapshot(
            id = "ID",
            url = "https://example.com",
            title = "ARTICLE",
            authorId = "USER",
            authorIconUrl = "https://example.com",
            createdAt = LocalDateTime.of(2020, 10, 20, 12, 30, 45),
            tags = listOf("Android"),
            likes = 1,
        )
}
