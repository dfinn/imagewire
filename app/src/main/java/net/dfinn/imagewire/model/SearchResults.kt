package net.dfinn.imagewire.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SearchResults(
    val data: List<GalleryItem>,
    val success: Boolean,
    val status: Int
)

@kotlinx.serialization.Serializable
/**
 * The GalleryItem represents a single search result, which can be either an album, or an individual
 * image.  In the case of an album, "cover" will be populated and will be the ID of the image
 * that serves as its cover image, and the images list will be populated.  In the case of an
 * individual photo, neither cover nor images will be included.  The isAlbum field can be used
 * to differentiate the two kinds of search result.
 **/
data class GalleryItem(
    val id: String,
    val title: String?,
    val cover: String? = null,
    @SerialName("is_album") val isAlbum: Boolean,
    val images: List<GalleryImage>? = null
) {
    /** The ID of the image used to represent this gallery item. In the case of albums, it will
     *  be the ID of the cover image. Otherwise, it will just be the imge ID.
     */
    val imageId: String
        get() = if (isAlbum && cover != null) cover else id
}

@kotlinx.serialization.Serializable
data class GalleryImage(
    val id: String,
    val title: String?,
)