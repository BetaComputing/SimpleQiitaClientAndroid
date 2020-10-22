package net.aridai.simpleqiitaclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_activity.*
import net.aridai.simpleqiitaclient.ui.article.Article
import net.aridai.simpleqiitaclient.ui.article.ArticleClickedListener
import net.aridai.simpleqiitaclient.ui.article.tag.TagClickedListener
import net.aridai.simpleqiitaclient.ui.databinding.ArticleViewBinding
import net.aridai.simpleqiitaclient.ui.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder

internal class MainActivity : AppCompatActivity(), ArticleClickedListener, TagClickedListener {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainActivityBinding

    private val adapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).also {
            this.binding = it
            it.lifecycleOwner = this
            it.viewModel = this.viewModel
        }

        this.articleList.adapter = this.adapter
        this.viewModel.articleList.observe(this, this::updateArticleList)
    }

    override fun onArticleClicked(article: Article) {
        this.launchUrl(article.url)
    }

    override fun onTagClicked(tag: String) {
        val encodedTag = URLEncoder.encode(tag, Charsets.UTF_8.name())
        val url = "https://qiita.com/tags/$encodedTag"

        this.launchUrl(url)
    }

    private fun updateArticleList(newList: List<Article>) {
        this.adapter.articleList = newList
        this.adapter.notifyDataSetChanged()
    }

    private fun launchUrl(url: String) {
        val uri = Uri.parse(url)
        this.startActivity(Intent(Intent.ACTION_VIEW, uri))
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
