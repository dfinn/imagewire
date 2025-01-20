package net.dfinn.imagewire.navigation

import kotlinx.coroutines.flow.SharedFlow

interface Navigator {
    val navActions: SharedFlow<NavAction>
    fun navigateBack()
    fun navigateToAlbum(albumId: String)
    fun navigateToImage(imageId: String)
}