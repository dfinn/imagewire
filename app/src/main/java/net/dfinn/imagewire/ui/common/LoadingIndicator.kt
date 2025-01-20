package net.dfinn.imagewire.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val modifier = Modifier.size(100.dp)
        if (LocalInspectionMode.current) {
            CircularProgressIndicator(modifier = modifier, progress = { 0.9f })
        } else {
            CircularProgressIndicator(modifier = modifier)
        }
    }
}
