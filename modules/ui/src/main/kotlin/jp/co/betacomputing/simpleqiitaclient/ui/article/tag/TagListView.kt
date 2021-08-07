package jp.co.betacomputing.simpleqiitaclient.ui.article.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow

//  タグのリスト
//  (FlexLayout風に配置される。)
@Composable
internal fun TagListView(
    modifier: Modifier = Modifier,
    tags: List<String>,
    onTagClicked: TagClickedListener,
) {
    FlowRow(modifier = modifier) {
        for (tag in tags) {
            TagView(tag = tag, onClick = { onTagClicked.onTagClicked(tag) })
        }
    }
}

//  タグ
@Composable
private fun TagView(tag: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(CornerSize(8.dp))

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .background(color = Color(0xFFCCCCCC), shape = shape)
            .clip(shape = shape),
    ) {
        Text(
            text = tag,
            fontSize = 15.sp,
            color = Color(0xFF333333),
            overflow = TextOverflow.Visible,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = onClick,
                )
                .padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun TagListViewPreview() {
    TagListView(
        tags = List(size = 10) { "記事タグ${it + 1}" },
        onTagClicked = {},
    )
}
