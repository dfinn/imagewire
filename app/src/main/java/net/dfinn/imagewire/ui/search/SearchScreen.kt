package net.dfinn.imagewire.ui.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import net.dfinn.imagewire.R
import net.dfinn.imagewire.api.ImgurApi
import net.dfinn.imagewire.model.GalleryImage
import net.dfinn.imagewire.model.GalleryItem
import net.dfinn.imagewire.ui.common.ErrorText
import net.dfinn.imagewire.ui.common.LoadingIndicator
import net.dfinn.imagewire.util.CustomPreviews

@Composable
fun SearchScreen() {
    // Get existing ViewModel, or create new one scoped to the current navigation graph.
    val searchViewModel: SearchViewModel = hiltViewModel()

    val uiState = searchViewModel.uiState.collectAsStateWithLifecycle().value
    val searchQuery = searchViewModel.searchQuery
    val focusManager = LocalFocusManager.current

    SearchScreenContents(uiState,
        searchQuery,
        onQueryTextChanged = { searchViewModel.onEvent(SearchUiEvent.QueryTextChanged(it)) },
        onSearchButtonClicked = {
            searchViewModel.onEvent(SearchUiEvent.SearchButtonClicked)
            // Close the on-screen keyboard
            focusManager.clearFocus()
        },
        onGalleryItemClicked = { searchViewModel.onEvent(SearchUiEvent.GalleryItemClicked(it)) }
    )
}

@Composable
fun SearchScreenContents(
    uiState: SearchUiState,
    searchQuery: String,
    onQueryTextChanged: (String) -> Unit,
    onSearchButtonClicked: () -> Unit,
    onGalleryItemClicked: (GalleryItem) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_outer))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryTextChanged,
                modifier = Modifier.requiredWidth(250.dp),
                label = { Text(stringResource(R.string.search_query)) })

            Button(
                onClick = onSearchButtonClicked,
                enabled = uiState.isSearchButtonEnabled
            ) {
                Icon(Icons.Filled.Search, stringResource(R.string.search))
            }
        }

        if (uiState.searchResults != null) {
            SearchResultsList(uiState.searchResults, onGalleryItemClicked = onGalleryItemClicked)
        }
    }
}

@Composable
fun SearchResultsList(
    searchResults: Pager<Int, GalleryItem>,
    onGalleryItemClicked: (GalleryItem) -> Unit
) {
    val lazyPagingItems = searchResults.flow.collectAsLazyPagingItems()
    val state = lazyPagingItems.loadState

    if (state.source.hasError) {
        ErrorText(R.string.error_during_search)
    } else if (state.refresh == LoadState.Loading) {
        LoadingIndicator()
    } else if (state.source.refresh is LoadState.NotLoading && state.append.endOfPaginationReached && lazyPagingItems.itemCount == 0) {
        ErrorText(R.string.no_results_found)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(count = lazyPagingItems.itemCount) { index ->
                val galleryItem = lazyPagingItems[index]
                if (galleryItem != null) {
                    GalleryAsyncImage(
                        url = ImgurApi.urlForImageThumbnail(galleryItem.imageId),
                        onClick = { onGalleryItemClicked(galleryItem) })
                } else {
                    // If GalleryItem is null (not loaded yet in the lazyPagingItems), use empty url
                    // so we just show the placeholder image for now.
                    GalleryAsyncImage(url = "", onClick = { })
                }
            }
        }
    }
}

@Composable
fun GalleryAsyncImage(url: String, onClick: () -> Unit) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        modifier = Modifier
            .padding(4.dp)
            .border(width = 1.dp, color = Color.Black)
            .size(120.dp)
            .padding(4.dp)
            .clickable(onClick = onClick),
        contentDescription = null,
        placeholder = painterResource(R.drawable.photo_placeholder),
        fallback = ColorPainter(Color.Red),
        contentScale = ContentScale.Crop
    )
}

@CustomPreviews
@Composable
fun Preview_SearchEnabled() {
    SearchScreenContents(
        uiState = SearchUiState(
            isSearchButtonEnabled = true,
            error = null,
            searchResults = null
        ),
        searchQuery = "forklift",
        onQueryTextChanged = {},
        onSearchButtonClicked = {},
        onGalleryItemClicked = {}
    )
}

@CustomPreviews
@Composable
fun Preview_SearchDisabled() {
    SearchScreenContents(
        uiState = SearchUiState(
            isSearchButtonEnabled = false,
            error = null,
            searchResults = null
        ),
        searchQuery = "",
        onQueryTextChanged = {},
        onSearchButtonClicked = {},
        onGalleryItemClicked = {}
    )
}

@CustomPreviews
@Composable
fun Preview_SearchResults() {
    val sampleData = listOf(
        GalleryItem(
            id = "a0001",
            title = "Green tractor",
            cover = "i0001",
            isAlbum = true,
            images = listOf(GalleryImage(id = "i0001", title = ""))
        ),
        GalleryItem(
            id = "a0002",
            title = "Yellow tractor",
            cover = "i0002",
            isAlbum = true,
            images = listOf(GalleryImage(id = "i0002", title = ""))
        ),
    )

    /*
    SearchScreenContents(
        uiState = SearchUiState(
            isSearchButtonEnabled = false,
            error = null,
            searchResults = sampleData
        ),
        searchQuery = "forklift",
        onQueryTextChanged = {},
        onSearchButtonClicked = {}
    )

     */
}