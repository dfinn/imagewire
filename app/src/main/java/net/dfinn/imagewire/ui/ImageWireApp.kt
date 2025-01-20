package net.dfinn.imagewire.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import net.dfinn.imagewire.navigation.NavigateBack
import net.dfinn.imagewire.navigation.NavigateTo
import net.dfinn.imagewire.navigation.Navigator
import net.dfinn.imagewire.ui.image.ImageViewerScreen
import net.dfinn.imagewire.ui.search.SearchScreen
import net.dfinn.imagewire.ui.theme.ImageWireTheme
import net.dfinn.imagewire.util.Logger

sealed class NavDestination {
    @Serializable
    data object Search : NavDestination()

    @Serializable
    data object Album : NavDestination()

    @Serializable
    data class Image(val id: String) : NavDestination()
}


@Composable
fun ImageWireApp(navigator: Navigator, logger: Logger, finishActivity: () -> Unit) {
    val navController = rememberNavController()

    LaunchedEffect("navigation") {
        navigator.navActions.onEach { navAction ->
            logger.logInfo("Got navAction $navAction")
            when (navAction) {
                is NavigateTo -> navController.navigate(navAction.navDestination)
                is NavigateBack -> {
                    if (!navController.popBackStack()) {
                        finishActivity()
                    }
                }
            }
        }.launchIn(this)
    }

    ImageWireTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = NavDestination.Search
                ) {
                    composable<NavDestination.Search> { SearchScreen() }
                    composable<NavDestination.Image> { backStackEntry ->
                        val image: NavDestination.Image = backStackEntry.toRoute()
                        ImageViewerScreen(image.id)
                    }
                }
            }
        }
    }
}
