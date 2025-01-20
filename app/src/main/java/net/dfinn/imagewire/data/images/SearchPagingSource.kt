package net.dfinn.imagewire.data.images

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.dfinn.imagewire.api.ImgurApi
import net.dfinn.imagewire.model.ApiException
import net.dfinn.imagewire.model.GalleryItem
import net.dfinn.imagewire.util.Logger

class SearchPagingSource(
    private val imgurApi: ImgurApi,
    private val logger: Logger,
    private val query: String
) :
    PagingSource<Int, GalleryItem>() {

    companion object {
        private const val INITIAL_PAGE = 0
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryItem>): Int? {
        // If the data set is invalidated, start load at the beginning.  However we should never reach
        // this case since we are not returning LoadResult.Invalid() from anywhere in our load()
        return INITIAL_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val page = params.key ?: INITIAL_PAGE
        logger.logInfo("Loading page: $page")
        try {
            val response = imgurApi.search(page = page, query = query)
            return if (response.isSuccessful) {
                logger.logInfo("Response successful")
                val body = response.body()
                if (body != null) {
                    LoadResult.Page(
                        data = body.data,
                        prevKey = if (page == INITIAL_PAGE) null else page,
                        nextKey = if (body.data.isEmpty()) null else page + 1
                    )
                } else {
                    val errMsg = "Error in search: empty response"
                    logger.logError(errMsg)
                    LoadResult.Error(ApiException(errMsg))
                }
            } else {
                val errMsg =
                    "Error in search (code=${response.code()}): ${response.errorBody()?.string()}"
                logger.logError(errMsg)
                LoadResult.Error(ApiException(errMsg))
            }
        } catch (ex: Throwable) {
            val errMsg = "Exception during search: ${ex.message}"
            logger.logError(errMsg, ex)
            return LoadResult.Error(ex)
        }
    }
}