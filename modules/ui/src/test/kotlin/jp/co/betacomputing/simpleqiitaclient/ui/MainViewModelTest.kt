package jp.co.betacomputing.simpleqiitaclient.ui

import android.os.Build
import androidx.lifecycle.Observer
import io.mockk.*
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchResponse
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSearchUseCase
import jp.co.betacomputing.simpleqiitaclient.application.article.ArticleSnapshot
import jp.co.betacomputing.simpleqiitaclient.common.observeEventForever
import jp.co.betacomputing.simpleqiitaclient.ui.article.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
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

    private lateinit var testCoroutineDispatcher: TestCoroutineDispatcher
    private lateinit var mockArticleSearchUseCase: ArticleSearchUseCase

    private val mockArticleListObserver: Observer<List<Article>?> = mockk(relaxed = true)
    private val mockKeywordObserver: Observer<String> = mockk(relaxed = true)
    private val mockIsSearchButtonEnabledObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockIsProgressBarVisibleObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockIsInitialMessageVisibleObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockIsEmptyMessageVisibleObserver: Observer<Boolean> = mockk(relaxed = true)
    private val mockFailedEventObserver: Observer<Unit> = mockk(relaxed = true)
    private val mockKeyboardHiddenRequestEventObserver: Observer<Unit> = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        testCoroutineDispatcher = TestCoroutineDispatcher()
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
    fun `test初期状態では初期メッセージが表示されているはず`() {
        //  初期状態で処理メッセージを表示することになっているはず。
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
    }

    @Test
    fun `test検索キーワードに1文字以上の非空白文字列が設定されているときのみ検索ボタンが有効であるはず`() {
        //  検索キーワードが空白の場合、検索ボタンは無効であるはず。
        Assert.assertEquals("", viewModel.keyword.value)
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  1文字でも検索キーワードが入力されている場合、検索ボタンは有効であるはず。
        viewModel.keyword.value = "あ"
        Assert.assertEquals("あ", viewModel.keyword.value)
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)

        //  ただし、空白文字列の場合、検索ボタンは無効であるはず。
        viewModel.keyword.value = " "
        Assert.assertEquals(" ", viewModel.keyword.value)
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  複数の空白文字でも無効なはず。
        viewModel.keyword.value = "  "
        Assert.assertEquals("  ", viewModel.keyword.value)
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)

        //  複数文字の非空白文字列を検索キーワードに入力している場合、検索ボタンは有効であるはず。
        viewModel.keyword.value = "あいう"
        Assert.assertEquals("あいう", viewModel.keyword.value)
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)
    }

    @Test
    fun `test検索ボタンを押すとキーボードが非表示になるはず`() {
        //  何もしていないときはキーボード非表示要求は一度も発火していないはず。
        verify(exactly = 0) { mockKeyboardHiddenRequestEventObserver.onChanged(Unit) }

        //  検索ボタンを押すとキーボード非表示要求が投げられるはず。
        viewModel.onSearchButtonClicked()
        verify(exactly = 1) { mockKeyboardHiddenRequestEventObserver.onChanged(Unit) }

        //  何かしらの検索キーワードを入力していても非表示要求が投げられるはず。
        viewModel.keyword.value = "なにかしらの検索"
        viewModel.onSearchButtonClicked()
        verify(exactly = 2) { mockKeyboardHiddenRequestEventObserver.onChanged(Unit) }
    }

    @Test
    fun `test検索中はプログレスバーが表示されているはず`() {
        //  検索処理に時間がかかるようにモックを設定する。
        val delay = 1000L
        coEvery { mockArticleSearchUseCase.execute(any()) } coAnswers {
            delay(delay)
            ArticleSearchResponse.Success(listOf(createDummyArticleSnapshot()))
        }

        //  何かしらの検索キーワードを入力して、検索ボタンを有効化させる。
        val keyword = "てすと"
        viewModel.keyword.value = keyword
        Assert.assertEquals(keyword, viewModel.keyword.value)
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)

        //  処理開始前は初期メッセージが表示され、プログレスバーが表示されていないはず。
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)

        //  検索ボタンを押す。
        viewModel.onSearchButtonClicked()

        //  検索処理が走ったはず。
        //  (処理はsuspend関数で一時中断させている。)
        coVerify(exactly = 1) { mockArticleSearchUseCase.execute(match { it.keyword == keyword }) }
        confirmVerified(mockArticleSearchUseCase)

        //  処理中は初期メッセージが非表示にされ、検索ボタンは無効化され、プログレスバーが表示されているはず。
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isSearchButtonEnabled.value)
        Assert.assertEquals(true, viewModel.isProgressBarVisible.value)

        //  検索処理を進めて終わらせる。
        //  (処理結果は成功扱いとなる。)
        testCoroutineDispatcher.advanceTimeBy(delay)

        //  処理成功後は初期メッセージもプログレスバーも非表示になり、検索ボタンが再び有効になるはず。
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(true, viewModel.isSearchButtonEnabled.value)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
    }

    @Test
    fun `test検索結果が存在する場合は記事リストが表示されているはず`() {
        //  記事の取得処理が成功するようにモックを設定する。
        val resultList = listOf(createDummyArticleSnapshot())
        coEvery { mockArticleSearchUseCase.execute(any()) } returns ArticleSearchResponse.Success(resultList)

        //  最初は初期メッセージが表示されている状態になっているはず。
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 0) { mockFailedEventObserver.onChanged(Unit) }

        //  検索キーワードを適当に入れて検索を処理を掛ける。
        //  処理はそのまま完了させる。
        viewModel.keyword.value = "きーわーど"
        viewModel.onSearchButtonClicked()

        //  検索処理が成功すると、プログレスバーも初期画面も非表示となり、記事が空っぽではないため、空メッセージも表示されていないはず。
        Assert.assertEquals(true, viewModel.articleList.value!!.isNotEmpty())
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 0) { mockFailedEventObserver.onChanged(Unit) }
    }

    @Test
    fun `test検索結果が空である場合は空メッセージが表示されているはず`() {
        //  記事の取得処理が成功し、空の結果を返すようにモックを設定する。
        val resultList = emptyList<ArticleSnapshot>()
        coEvery { mockArticleSearchUseCase.execute(any()) } returns ArticleSearchResponse.Success(resultList)

        //  最初は初期メッセージが表示されている状態になっているはず。
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 0) { mockFailedEventObserver.onChanged(Unit) }

        //  検索キーワードを適当に入れて検索を処理を掛ける。
        //  処理はそのまま完了させる。
        viewModel.keyword.value = "きーわーど"
        viewModel.onSearchButtonClicked()

        //  検索処理が成功すると、プログレスバーも初期画面も非表示となり、記事が空っぽであるため、空メッセージも表示されるはず。
        Assert.assertEquals(true, viewModel.articleList.value!!.isEmpty())
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(true, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 0) { mockFailedEventObserver.onChanged(Unit) }
    }

    @Test
    fun `test検索に失敗した場合は失敗イベントが発行され初期メッセージが表示されているはず`() {
        //  記事の取得処理が失敗するようにモックを設定する。
        coEvery { mockArticleSearchUseCase.execute(any()) } returns ArticleSearchResponse.Failure

        //  最初は初期メッセージが表示されている状態になっているはず。
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 0) { mockFailedEventObserver.onChanged(Unit) }

        //  検索キーワードを適当に入れて検索を処理を掛ける。
        //  処理はそのまま完了させる。
        viewModel.keyword.value = "きーわーど"
        viewModel.onSearchButtonClicked()

        //  検索処理が失敗すると、プログレスバーも空メッセージも非表示となり、初期画面が表示され、失敗イベントが発火されるはず。
        Assert.assertNull(viewModel.articleList.value)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 1) { mockFailedEventObserver.onChanged(Unit) }
    }

    @Test
    fun `test複数回検索を繰り返すケース`() {
        val keyword1 = "1.失敗するケース"
        coEvery {
            mockArticleSearchUseCase.execute(match { it.keyword == keyword1 })
        } returns ArticleSearchResponse.Failure

        val keyword2 = "2.非空リストのケース"
        val resultList2 = List(size = 2) { createDummyArticleSnapshot() }
        coEvery {
            mockArticleSearchUseCase.execute(match { it.keyword == keyword2 })
        } returns ArticleSearchResponse.Success(resultList2)

        val keyword3 = "3.空リストのケース"
        val resultList3 = emptyList<ArticleSnapshot>()
        coEvery {
            mockArticleSearchUseCase.execute(match { it.keyword == keyword3 })
        } returns ArticleSearchResponse.Success(resultList3)

        val keyword4 = "4.失敗するケース"
        coEvery {
            mockArticleSearchUseCase.execute(match { it.keyword == keyword4 })
        } returns ArticleSearchResponse.Failure

        val keyword5 = "5.非空リストのケース"
        val resultList5 = List(size = 5) { createDummyArticleSnapshot() }
        coEvery {
            mockArticleSearchUseCase.execute(match { it.keyword == keyword5 })
        } returns ArticleSearchResponse.Success(resultList5)

        //  1回目の検索を行う。
        viewModel.keyword.value = keyword1
        viewModel.onSearchButtonClicked()

        //  失敗して初期メッセージが表示されるはず。
        Assert.assertNull(viewModel.articleList.value)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 1) { mockFailedEventObserver.onChanged(Unit) }

        //  2回目の検索を行う。
        viewModel.keyword.value = keyword2
        viewModel.onSearchButtonClicked()

        //  非空リストの取得に成功するはず。
        Assert.assertEquals(resultList2.size, viewModel.articleList.value!!.size)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 1) { mockFailedEventObserver.onChanged(Unit) }

        //  3回目の検索を行う。
        viewModel.keyword.value = keyword3
        viewModel.onSearchButtonClicked()

        //  空リストの取得に成功するはず。
        Assert.assertEquals(true, viewModel.articleList.value!!.isEmpty())
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(true, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 1) { mockFailedEventObserver.onChanged(Unit) }

        //  4回目の検索を行う。
        viewModel.keyword.value = keyword4
        viewModel.onSearchButtonClicked()

        //  失敗して初期メッセージが表示されるはず。
        Assert.assertNull(viewModel.articleList.value)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(true, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 2) { mockFailedEventObserver.onChanged(Unit) }

        //  5回目の検索を行う。
        viewModel.keyword.value = keyword5
        viewModel.onSearchButtonClicked()

        Assert.assertEquals(resultList5.size, viewModel.articleList.value!!.size)
        Assert.assertEquals(false, viewModel.isProgressBarVisible.value)
        Assert.assertEquals(false, viewModel.isInitialMessageVisible.value)
        Assert.assertEquals(false, viewModel.isEmptyMessageVisible.value)
        verify(exactly = 2) { mockFailedEventObserver.onChanged(Unit) }
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
        viewModel.isInitialMessageVisible.observeForever(mockIsInitialMessageVisibleObserver)
        viewModel.isEmptyMessageVisible.observeForever(mockIsEmptyMessageVisibleObserver)
        viewModel.failedEvent.observeEventForever(mockFailedEventObserver)
        viewModel.keyboardHiddenRequestEvent.observeEventForever(mockKeyboardHiddenRequestEventObserver)
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
