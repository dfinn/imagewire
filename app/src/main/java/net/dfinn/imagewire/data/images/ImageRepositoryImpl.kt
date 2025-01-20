package net.dfinn.imagewire.data.images

import kotlinx.serialization.json.Json
import net.dfinn.imagewire.api.ClientIdHeaderInterceptor
import net.dfinn.imagewire.api.ImgurApi
import net.dfinn.imagewire.util.Logger
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val logger: Logger) : ImageRepository {
    private val okHttpClient = okhttp3.OkHttpClient.Builder()
        .addInterceptor(ClientIdHeaderInterceptor(ImgurApi.CLIENT_ID))
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ImgurApi.IMGUR_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
        .build()

    private val imgurApi = retrofit.create(ImgurApi::class.java)

    override fun search(query: String) = SearchPagingSource(imgurApi, logger, query)
}