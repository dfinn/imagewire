package net.dfinn.imagewire.navigation

import net.dfinn.imagewire.ui.NavDestination

sealed class NavAction
data class NavigateTo(val navDestination: NavDestination) : NavAction()
data object NavigateBack : NavAction()