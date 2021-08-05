package jp.co.betacomputing.simpleqiitaclient.ui.article

import android.content.Context
import jp.co.betacomputing.simpleqiitaclient.ui.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

internal object ArticleViewConverter {

    @JvmStatic
    fun convertLikeCount(context: Context, likes: Int): String = context.getString(R.string.likes_count, likes)

    @JvmStatic
    fun convertCreatedAt(context: Context, createdAt: LocalDateTime): String =
        createdAt.format(DateTimeFormatter.ofPattern(context.getString(R.string.created_at_pattern)))
}
