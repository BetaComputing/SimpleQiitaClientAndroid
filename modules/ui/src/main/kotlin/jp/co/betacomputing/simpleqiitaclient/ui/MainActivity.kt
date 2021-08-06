package jp.co.betacomputing.simpleqiitaclient.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jp.co.betacomputing.simpleqiitaclient.common.observeEvent
import jp.co.betacomputing.simpleqiitaclient.ui.article.Article
import jp.co.betacomputing.simpleqiitaclient.ui.article.ArticleClickedListener
import jp.co.betacomputing.simpleqiitaclient.ui.article.tag.TagClickedListener
import jp.co.betacomputing.simpleqiitaclient.ui.databinding.ArticleViewBinding
import jp.co.betacomputing.simpleqiitaclient.ui.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder

internal class MainActivity : AppCompatActivity(), ArticleClickedListener, TagClickedListener {

    private lateinit var inputMethodManager: InputMethodManager

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainActivityBinding

    private val adapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.inputMethodManager = this.getSystemService()!!

        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).also {
            this.binding = it
            it.lifecycleOwner = this
            it.viewModel = this.viewModel
        }

        this.binding.articleList.adapter = this.adapter
        this.viewModel.articleList.observe(this, this::updateArticleList)
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

        this.setContent {
            MaterialTheme {
                MainActivityContent()
            }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun updateArticleList(newList: List<Article>?) {
        this.adapter.articleList = newList ?: emptyList()
        this.adapter.notifyDataSetChanged()
    }

    private fun launchUrl(url: String) {
        val uri = Uri.parse(url)
        this.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun showSnackbar() {
        Snackbar.make(this.binding.outerLayout, R.string.search_failure_message, Snackbar.LENGTH_SHORT).show()
    }

    private inner class Adapter(private val parent: MainActivity = this) : RecyclerView.Adapter<BindingHolder>() {

        private val inflater: LayoutInflater by lazy { LayoutInflater.from(this.parent) }

        var articleList: List<Article> = emptyList()

        override fun getItemCount(): Int = this.articleList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
            val binding = ArticleViewBinding.inflate(this.inflater, parent, false)

            return BindingHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingHolder, position: Int) {
            holder.binding.article = this.articleList[position]
            holder.binding.articleClickeListener = this.parent
            holder.binding.tagClickedListener = this.parent
            holder.binding.executePendingBindings()
        }
    }

    private class BindingHolder(val binding: ArticleViewBinding) : RecyclerView.ViewHolder(binding.root)
}

@Composable
private fun MainActivityContent() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text("Hello World!")
        }
    }
}

@Preview
@Composable
private fun MainActivityContentPreview() {
    MaterialTheme {
        MainActivityContent()
    }
}
