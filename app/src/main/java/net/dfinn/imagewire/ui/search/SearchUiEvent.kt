package net.dfinn.imagewire.ui.search

import net.dfinn.imagewire.model.GalleryItem

sealed class SearchUiEvent {
    data class QueryTextChanged(val queryText: String) : SearchUiEvent()
    data object SearchButtonClicked : SearchUiEvent()
    data class GalleryItemClicked(val galleryItem: GalleryItem) : SearchUiEvent()
}