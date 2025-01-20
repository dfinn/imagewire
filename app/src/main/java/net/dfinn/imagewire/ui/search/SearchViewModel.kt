package net.dfinn.imagewire.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.dfinn.imagewire.data.images.ImageRepository
import net.dfinn.imagewire.domain.ValidateQueryTextUseCase
import net.dfinn.imagewire.model.GalleryItem
import net.dfinn.imagewire.navigation.Navigator
import net.dfinn.imagewire.util.Logger
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val logger: Logger,
    private val navigator: Navigator
) :
    ViewModel() {

    private val _uiState =
        MutableStateFlow(
            SearchUiState(
                isSearchButtonEnabled = false,
                error = null,
                searchResults = null
            )
        )
    val uiState: StateFlow<SearchUiState> = _uiState
    val validateQueryTextUseCase = ValidateQueryTextUseCase()

    var searchQuery by mutableStateOf("")
        private set

    fun onEvent(event: SearchUiEvent) {
        logger.logInfo("onEvent: $event")
        when (event) {
            is SearchUiEvent.QueryTextChanged -> validateSearchQuery(event)
            is SearchUiEvent.SearchButtonClicked -> doSearch()
            is SearchUiEvent.GalleryItemClicked -> navigateToGalleryItem(event.galleryItem)
        }
    }

    private fun validateSearchQuery(queryTextEvent: SearchUiEvent.QueryTextChanged) {
        searchQuery = queryTextEvent.queryText
        _uiState.value =
            _uiState.value.copy(isSearchButtonEnabled = validateQueryTextUseCase(queryTextEvent.queryText))
    }

    private fun doSearch() {
        logger.logInfo("Searching for: $searchQuery")
        _uiState.value =
            SearchUiState(isSearchButtonEnabled = false, error = null, searchResults = null)

        val pager = Pager(
            config = PagingConfig(pageSize = ImageRepository.SEARCH_PAGE_SIZE),
            pagingSourceFactory = { imageRepository.search(searchQuery) }
        )

        _uiState.value = SearchUiState(
            isSearchButtonEnabled = false,
            error = null,
            searchResults = pager
        )
    }

    private fun navigateToGalleryItem(galleryItem: GalleryItem) {
        // Navigate to full-screen view of the image by its ID.
        // If the item is an album, show its "cover" image.
        if (galleryItem.isAlbum) {
            if (galleryItem.cover != null) {
                navigator.navigateToImage(galleryItem.cover)
            } else {
                logger.logError("Unable to show album item ${galleryItem.id} - cover is null")
                // TODO: Raise some kind of error to the UI.
            }
        } else {
            navigator.navigateToImage(galleryItem.id)
        }
    }
}