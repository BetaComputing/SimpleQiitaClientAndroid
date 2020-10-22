package net.aridai.simpleqiitaclient.data.article

import com.squareup.moshi.Json

internal data class ArticleDto(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "title") val title: String,
    @Json(name = "user") val user: User,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "tags") val tags: List<Tag>,
    @Json(name = "likes_count") val likesCount: Int,
) {
    data class User(
        @Json(name = "id") val id: String,
        @Json(name = "profile_image_url") val profileImageUrl: String,
    )

    data class Tag(
        @Json(name = "name")
        val name: String,
    )
}
