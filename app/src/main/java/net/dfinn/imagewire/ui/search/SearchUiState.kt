package net.dfinn.imagewire.ui.search

import androidx.paging.Pager
import net.dfinn.imagewire.model.GalleryItem

data class SearchUiState(
    val isSearchButtonEnabled: Boolean,
    val error: Exception?,
    val searchResults: Pager<Int, GalleryItem>?
)