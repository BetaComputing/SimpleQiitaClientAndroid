package net.aridai.simpleqiitaclient.ui.article

import org.threeten.bp.LocalDateTime

internal data class Article(
    val id: String,
    val url: String,
    val title: String,
    val authorId: String,
    val authorIconUrl: String,
    val createdAt: LocalDateTime,
    val tags: List<String>,
    val likes: Int,
) {
    companion object {
        val DUMMY = Article(
            id = "cf9a7627c41bf7c13c80",
            url = "https://qiita.com/aridai/items/cf9a7627c41bf7c13c80",
            title = "【Android】プログラム的にBluetoothのペアリングを行う方法【Bluetooth】",
            authorId = "aridai",
            authorIconUrl = "https://s3-ap-northeast-1.amazonaws.com/qiita-image-store/0/" +
                "341119/d45dcff0ceabdf78fa082d872fea08492157dbef/large.png?1581068618",
            createdAt = LocalDateTime.now(),
            tags = listOf("Android", "Bluetooth"),
            likes = 0,
        )
    }
}
