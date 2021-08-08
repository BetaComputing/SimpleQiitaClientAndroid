package jp.co.betacomputing.simpleqiitaclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.getSystemService
import com.google.android.material.snackbar.Snackbar
import jp.co.betacomputing.simpleqiitaclient.common.observeEvent
import jp.co.betacomputing.simpleqiitaclient.ui.article.Article
import jp.co.betacomputing.simpleqiitaclient.ui.article.ArticleView
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class MainActivity : AppCompatActivity() {

    private lateinit var inputMethodManager: InputMethodManager
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.inputMethodManager = this.getSystemService()!!
        this.setContentView(R.layout.main_activity)

        //  UIを設定する。
        val viewModel = this.viewModel
        this.findViewById<ComposeView>(R.id.composeView).setContent {
            MainActivityContent(viewModel)
        }

        //  失敗時はSnackBarを表示するように設定する。
        viewModel.failedEvent.observeEvent(this) { this.showSnackBar() }

        //  キーボードの非表示要求で非表示処理を走らせる。
        viewModel.keyboardHiddenRequestEvent.observe(this) {
            val token = this.window.decorView.rootView.windowToken
            this.inputMethodManager.hideSoftInputFromWindow(token, 0)
        }

        //  Qiita記事ページへの遷移要求でブラウザを立ち上げさせる。
        viewModel.toArticlePageRequest.observeEvent(this) { url -> this.launchUrl(url) }

        //  Qiitaタグページへの遷移要求でブラウザを立ち上げさせる。
        viewModel.toTagPageRequest.observeEvent(this) { url -> this.launchUrl(url) }
    }

    //  URLをブラウザで開く。
    private fun launchUrl(url: String) {
        val uri = Uri.parse(url)
        this.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    //  SnackBarを表示させる。
    private fun showSnackBar() {
        //  イベントの都合上、置き換えがめんどくさいため、従来のViewのSnackbarを使用する。
        //  その場合、SnackBarを配置する親となるViewを渡す必要があるが、
        //  Jetpack Composeをそのまま使うと正しく配置されなくなる。
        //  (通常よりも低い位置に表示されてしまう。)
        //  そのため、従来のViewシステムでLinearLayoutの中にComposeViewを配置するような構造を作り、
        //  そこからComposeViewを取り出し、SnackBarの親として指定してやる。

        val view = this.findViewById<ComposeView>(R.id.composeView)
        Snackbar.make(view, R.string.search_failure_message, Snackbar.LENGTH_SHORT).show()
    }
}

//  MainActivityのUIコンテンツ
//  (基本的にActivity/Fragmentの閉じた範囲で使うComposable関数はファイル内にprivateで定義し、ViewModelをパラメタで受け渡ししてもよいことにする。
//  Activity/Fragmentの閉じた範囲の外にある、独立したUIパーツとしてのComposable関数については、ViewModelごとではなく、個別の値ごとを受け渡しできるようにする。)
@Composable
private fun MainActivityContent(viewModel: MainViewModel) {
    MaterialTheme {
        Scaffold(topBar = { AppBar() }) {
            Column {
                //  検索コントロール部分
                SearchControls(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel,
                )

                //  メインコンテンツ部分
                MainContents(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                )
            }
        }
    }
}

//  AppBar部分
@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.app_name)) },
    )
}

//  検索コントロール部分
@Composable
private fun SearchControls(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    ConstraintLayout(modifier = modifier) {
        val (fieldRef, buttonRef) = createRefs()
        val keyword: String by viewModel.keyword.observeAsState(initial = "")
        val isEnabled: Boolean by viewModel.isSearchButtonEnabled.observeAsState(initial = false)

        //  検索キーワード入力フィールド
        TextField(
            value = keyword,
            singleLine = true,
            keyboardActions = KeyboardActions(onSearch = { viewModel.onSearchButtonClicked() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            onValueChange = { newKeyword ->
                if (newKeyword != keyword) viewModel.keyword.value = newKeyword
            },
            placeholder = { Text(text = stringResource(R.string.search_button_placeholder)) },
            modifier = Modifier
                .constrainAs(fieldRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(buttonRef.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(8.dp),
        )

        //  検索ボタン
        Button(
            onClick = { viewModel.onSearchButtonClicked() },
            enabled = isEnabled,
            modifier = Modifier
                .constrainAs(buttonRef) {
                    start.linkTo(fieldRef.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(8.dp),
        ) {
            Text(text = stringResource(R.string.search_button))
        }
    }
}

//  メインコンテンツ部分
//  (記事リスト・メッセージ類・プログレスバー)
@Composable
private fun MainContents(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val isProgressBarVisible: Boolean by viewModel.isProgressBarVisible.observeAsState(initial = false)
        val isInitialMessageVisible: Boolean by viewModel.isInitialMessageVisible.observeAsState(initial = true)
        val isEmptyMessageVisible: Boolean by viewModel.isEmptyMessageVisible.observeAsState(initial = false)

        if (isProgressBarVisible) CircularProgressIndicator()
        else ArticleList(viewModel = viewModel)

        if (isInitialMessageVisible) Text(text = stringResource(R.string.initial_message))

        if (isEmptyMessageVisible) Text(text = stringResource(R.string.empty_message))
    }
}

//  記事リスト
@Composable
private fun ArticleList(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val articles: List<Article>? by viewModel.articleList.observeAsState(initial = null)

    LazyColumn(
        modifier = modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
    ) {
        items(items = articles ?: emptyList(), key = { article -> article.id }) { article ->
            ArticleView(
                article,
                onArticleClicked = viewModel::onArticleClicked,
                onTagClicked = viewModel::onTagClicked,
            )
        }
    }
}
