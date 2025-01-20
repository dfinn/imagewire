package net.dfinn.imagewire.ui.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import net.dfinn.imagewire.R
import net.dfinn.imagewire.api.ImgurApi
import net.dfinn.imagewire.ui.common.ErrorText
import net.dfinn.imagewire.ui.common.LoadingIndicator
import net.dfinn.imagewire.util.CustomPreviews

@Composable
fun ImageViewerScreen(imageId: String) {
    Box(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_outer))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ImgurApi.urlForImage(imageId))
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            loading = {
                LoadingIndicator()
            },
            error = {
                ErrorText(R.string.error_loading_image)
            }
        )
    }
}

@CustomPreviews
@Composable
fun Preview_ImageViewerScreen() {
    ImageViewerScreen("")
}