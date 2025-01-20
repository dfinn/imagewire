package net.dfinn.imagewire.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import net.dfinn.imagewire.ui.NavDestination
import net.dfinn.imagewire.util.Logger
import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val logger: Logger) : Navigator {
    private val _navActions = MutableSharedFlow<NavAction>(extraBufferCapacity = 1)
    override val navActions: SharedFlow<NavAction> = _navActions

    override fun navigateBack() {
        logger.logInfo("Navigating back")
        _navActions.tryEmit(NavigateBack)
    }

    override fun navigateToAlbum(albumId: String) {
        logger.logInfo("Navigating to album $albumId")
        _navActions.tryEmit(NavigateTo(NavDestination.Album))
    }

    override fun navigateToImage(imageId: String) {
        logger.logInfo("Navigating to image $imageId")
        _navActions.tryEmit(NavigateTo(NavDestination.Image(imageId)))
    }
}