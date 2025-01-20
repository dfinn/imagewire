package net.dfinn.imagewire.data.images

import androidx.paging.PagingSource
import net.dfinn.imagewire.api.ImgurApi
import net.dfinn.imagewire.model.GalleryItem

interface ImageRepository {
    companion object {
        const val SEARCH_PAGE_SIZE = ImgurApi.GALLERY_ITEMS_PAGE_SIZE
    }

    fun search(query: String): PagingSource<Int, GalleryItem>
}