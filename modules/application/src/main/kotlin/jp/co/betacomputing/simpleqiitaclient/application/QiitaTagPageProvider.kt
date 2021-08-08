package jp.co.betacomputing.simpleqiitaclient.application

import java.net.URLEncoder

class QiitaTagPageProvider {

    //  記事のタグをQiitaのタグページのURLに変換する。
    fun toQiitaTagPageUrl(tagName: String): String {
        val encodedTag = URLEncoder.encode(tagName, Charsets.UTF_8.name())
        val url = "https://qiita.com/tags/$encodedTag"

        return url
    }
}
