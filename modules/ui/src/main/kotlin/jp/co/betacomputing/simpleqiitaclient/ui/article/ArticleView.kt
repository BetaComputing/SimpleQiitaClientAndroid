package jp.co.betacomputing.simpleqiitaclient.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import jp.co.betacomputing.simpleqiitaclient.ui.article.tag.TagClickedListener
import jp.co.betacomputing.simpleqiitaclient.ui.article.tag.TagListView
import org.threeten.bp.LocalDateTime

//  記事
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ArticleView(
    article: Article,
    onArticleClicked: ArticleClickedListener,
    onTagClicked: TagClickedListener,
) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = { onArticleClicked.onArticleClicked(article) },
        indication = rememberRipple(),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            //  アイコン画像
            Image(
                painter = rememberImagePainter(article.authorIconUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(corner = CornerSize(4.dp))),
            )

            Column {
                //  記事タイトル
                Text(text = article.title, fontSize = 16.sp)

                //  タグリスト
                TagListView(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    tags = article.tags,
                    onTagClicked = onTagClicked,
                )

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //  いいね数
                    LikeCount(count = article.likes)

                    //  作成日時
                    Text(
                        text = ArticleViewConverter.convertCreatedAt(LocalContext.current, article.createdAt),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

//  いいね数
@Composable
private fun LikeCount(modifier: Modifier = Modifier, count: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        //  いいねアイコン
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color(0xFFEF5350),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(24.dp),
        )

        //  いいね数
        Text(
            text = ArticleViewConverter.convertLikeCount(LocalContext.current, count),
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ArticleViewPreview() {
    ArticleView(
        article = Article(
            id = "ID",
            url = "https://example.com",
            title = "テスト記事",
            authorId = "AUTHOR",
            authorIconUrl = "https://betacomputing.co.jp/wp-content/uploads/2021/07/favicon.png",
            createdAt = LocalDateTime.of(2021, 8, 7, 12, 30, 45),
            tags = listOf(),
            likes = 99,
        ),
        onArticleClicked = {},
        onTagClicked = {},
    )
}
