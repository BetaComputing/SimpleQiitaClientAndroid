package jp.co.betacomputing.simpleqiitaclient.ui.article.tag

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import jp.co.betacomputing.simpleqiitaclient.ui.R
import jp.co.betacomputing.simpleqiitaclient.ui.databinding.TagBinding

internal class TagList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr), TagClickedListener {

    internal var listener: TagClickedListener? = null
    private val adapter = Adapter(this)

    init {
        val view = LayoutInflater.from(this.context).inflate(R.layout.tag_list, this, true)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val layoutManager = FlexboxLayoutManager(this.context, FlexDirection.ROW)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = this.adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun setTags(tags: List<String>) {
        this.adapter.tags = tags
        this.adapter.notifyDataSetChanged()
    }

    override fun onTagClicked(tag: String) {
        this.listener?.onTagClicked(tag)
    }

    private inner class Adapter(private val parent: TagList = this) : RecyclerView.Adapter<BindingHolder>() {

        private val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }

        var tags: List<String> = emptyList()

        override fun getItemCount(): Int = this.tags.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
            val binding = TagBinding.inflate(this.inflater, parent, false)

            return BindingHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingHolder, position: Int) {
            holder.binding.tag = this.tags[position]
            holder.binding.listener = this.parent
            holder.binding.executePendingBindings()
        }
    }

    private class BindingHolder(val binding: TagBinding) : RecyclerView.ViewHolder(binding.root)
}

@BindingAdapter("tags")
internal fun TagList.setTags(tags: List<String>?) {
    if (tags != null) {
        this.setTags(tags)
    }
}

@BindingAdapter("onTagClicked")
internal fun TagList.setListener(listener: TagClickedListener?) {
    this.listener = listener
}
