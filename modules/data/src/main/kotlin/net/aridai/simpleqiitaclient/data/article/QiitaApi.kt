package net.aridai.simpleqiitaclient.data.article

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

internal interface QiitaApi {

    @GET("v2/items")
    suspend fun search(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): List<ArticleDto>

    companion object {
        private const val BASE_URL = "https://qiita.com/api/"

        fun create(): QiitaApi {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create()
        }
    }
}
