package net.dfinn.imagewire.ui.search

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import net.dfinn.imagewire.data.images.ImageRepository
import net.dfinn.imagewire.model.GalleryImage
import net.dfinn.imagewire.model.GalleryItem
import net.dfinn.imagewire.navigation.Navigator
import net.dfinn.imagewire.ui.search.FakePagingSource.Companion.makeSampleData
import net.dfinn.imagewire.util.Logger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SearchViewModelTest {
    private val fakeImageRepository = object : ImageRepository {
        override fun search(query: String) = FakePagingSource()
    }

    private val consoleLogger = object : Logger {
        override fun logInfo(message: String) {
            println(message)
        }

        override fun logError(message: String, error: Throwable?) {
            if (error != null) {
                println("[error] $message: $error")
            } else {
                println("[error] $message")
            }
        }
    }

    private val mockNavigator = mock<Navigator>()

    private val viewModel = SearchViewModel(fakeImageRepository, consoleLogger, mockNavigator)

    @Test
    fun searchButtonNotEnabledWhenEmptyQuery() {
        assertFalse(viewModel.uiState.value.isSearchButtonEnabled)
    }

    @Test
    fun searchButtonNotEnabledWhenQueryOnlyWhitespace() {
        viewModel.onEvent(SearchUiEvent.QueryTextChanged(" "))
        assertFalse(viewModel.uiState.value.isSearchButtonEnabled)
    }

    @Test
    fun searchButtonEnabledWhenValidQuery() {
        viewModel.onEvent(SearchUiEvent.QueryTextChanged("tunnel"))
        assertTrue(viewModel.uiState.value.isSearchButtonEnabled)
    }

    @Test
    fun searchResultsReturnedWhenSearchExecuted() = runTest {
        viewModel.onEvent(SearchUiEvent.QueryTextChanged("tunnel"))
        assertTrue(viewModel.uiState.value.searchResults == null)
        viewModel.onEvent(SearchUiEvent.SearchButtonClicked)
        assertTrue(viewModel.uiState.value.searchResults != null)
        val items: List<GalleryItem> = viewModel.uiState.value.searchResults!!.flow.asSnapshot()
        val expectedItems = makeSampleData(0) + makeSampleData(1)
        assertEquals(expectedItems, items)
    }

    @Test
    fun navigatesToCoverImageWhenAlbumItemClicked() {
        viewModel.onEvent(SearchUiEvent.QueryTextChanged("tunnel"))
        viewModel.onEvent(SearchUiEvent.SearchButtonClicked)
        val sampleItem = GalleryItem(
            id = "id001",
            title = "Tractor",
            cover = "image001",
            isAlbum = true,
            images = listOf(GalleryImage("image001", null))
        )
        viewModel.onEvent(SearchUiEvent.GalleryItemClicked(sampleItem))
        verify(mockNavigator).navigateToImage("image001")
    }

    @Test
    fun navigatesToImageWhenImageItemClicked() {
        viewModel.onEvent(SearchUiEvent.QueryTextChanged("tunnel"))
        viewModel.onEvent(SearchUiEvent.SearchButtonClicked)
        val sampleItem = GalleryItem(
            id = "id001",
            title = "Tractor",
            cover = null,
            isAlbum = false,
            images = null
        )
        viewModel.onEvent(SearchUiEvent.GalleryItemClicked(sampleItem))
        verify(mockNavigator).navigateToImage("id001")
    }
}