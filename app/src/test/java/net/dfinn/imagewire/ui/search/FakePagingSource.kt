package net.dfinn.imagewire.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.dfinn.imagewire.model.GalleryImage
import net.dfinn.imagewire.model.GalleryItem

class FakePagingSource() : PagingSource<Int, GalleryItem>() {
    companion object {
        private const val ITEMS_PER_PAGE = 50

        fun makeSampleData(key: Int) =
            ((ITEMS_PER_PAGE * key)..<(ITEMS_PER_PAGE * key) + ITEMS_PER_PAGE).map { itemNum ->
                val images = (0..5).map { imageNum ->
                    GalleryImage(id = "$itemNum-$imageNum", "Image $imageNum")
                }
                GalleryItem(
                    id = "$itemNum",
                    title = "Gallery Item $itemNum",
                    cover = "Cover $itemNum",
                    isAlbum = true,
                    images = images
                )
            }
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryItem>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val currentKey = params.key ?: 0
        return LoadResult.Page(
            data = makeSampleData(currentKey),
            prevKey = null,
            nextKey = currentKey + 1
        )
    }
}