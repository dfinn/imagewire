package net.dfinn.imagewire.api

import net.dfinn.imagewire.model.SearchResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurApi {
    companion object {
        const val IMGUR_BASE_URL = "https://api.imgur.com"
        const val IMGUR_IMAGE_URL = "https://i.imgur.com"
        const val CLIENT_ID = "4371710316e975a"

        // The  /gallery endpoints do not support the perPage query string, so it will always be 50.
        // See "Paging Results" at https://apidocs.imgur.com/#intro
        const val GALLERY_ITEMS_PAGE_SIZE = 50

        // Helper function to build the URL for fetching a specific image
        fun urlForImage(id: String) = "${ImgurApi.IMGUR_IMAGE_URL}/${id}.jpg"

        // Appending the "m" suffix will fetch the medium size thumbnail at 320x320
        // See: https://api.imgur.com/models/image
        fun urlForImageThumbnail(id: String) = "${ImgurApi.IMGUR_IMAGE_URL}/${id}m.jpg"
    }

    @GET("3/gallery/search/time/all/{page}")
    suspend fun search(@Path("page") page: Int, @Query("q") query: String): Response<SearchResults>
}