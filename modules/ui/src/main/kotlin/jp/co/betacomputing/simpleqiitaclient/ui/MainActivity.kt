package jp.co.betacomputing.simpleqiitaclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import jp.co.betacomputing.simpleqiitaclient.common.observeEvent
import jp.co.betacomputing.simpleqiitaclient.ui.article.Article
import jp.co.betacomputing.simpleqiitaclient.ui.article.ArticleClickedListener
import jp.co.betacomputing.simpleqiitaclient.ui.article.ArticleView
import jp.co.betacomputing.simpleqiitaclient.ui.article.tag.TagClickedListener
import jp.co.betacomputing.simpleqiitaclient.ui.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder

internal class MainActivity : AppCompatActivity(), ArticleClickedListener, TagClickedListener {

    private lateinit var inputMethodManager: InputMethodManager

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.inputMethodManager = this.getSystemService()!!

        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).also {
            this.binding = it
            it.lifecycleOwner = this
            it.viewModel = this.viewModel
        }

        this.viewModel.failedEvent.observeEvent(this) { this.showSnackbar() }
        this.viewModel.keyboardHiddenRequestEvent.observe(this) {
            this.inputMethodManager.hideSoftInputFromWindow(this.binding.root.windowToken, 0)
        }

        //  入力フィールドのアクションが発生したとき。
        this.binding.keywordEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> this.viewModel.onSearchButtonClicked().let { true }
                else -> false
            }
        }

        //  一時的に記事リスト部分のみJetpack Composeで実装する。
        this.binding.articleList.setContent {
            ArticleList(viewModel = this.viewModel, onArticleClicked = this, onTagClicked = this)
        }
    }

    override fun onArticleClicked(article: Article) {
        this.launchUrl(article.url)
    }

    override fun onTagClicked(tag: String) {
        val encodedTag = URLEncoder.encode(tag, Charsets.UTF_8.name())
        val url = "https://qiita.com/tags/$encodedTag"

        this.launchUrl(url)
    }

    private fun launchUrl(url: String) {
        val uri = Uri.parse(url)
        this.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun showSnackbar() {
        Snackbar.make(this.binding.outerLayout, R.string.search_failure_message, Snackbar.LENGTH_SHORT).show()
    }
}

@Composable
private fun ArticleList(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onArticleClicked: ArticleClickedListener,
    onTagClicked: TagClickedListener,
) {
    val articles: List<Article>? by viewModel.articleList.observeAsState(initial = null)

    LazyColumn(
        modifier = modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical),
    ) {
        items(articles ?: emptyList()) { article ->
            ArticleView(article, onArticleClicked, onTagClicked)
        }
    }
}
