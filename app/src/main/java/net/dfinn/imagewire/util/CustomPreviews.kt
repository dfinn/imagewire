package net.dfinn.imagewire.util

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "NEXUS_5", device = Devices.NEXUS_5)
@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL)
@Preview(name = "PIXEL_TABLET", device = Devices.PIXEL_TABLET)
/** Custom preview annotation to show a very small phone, a larger phone, and a tablet **/
annotation class CustomPreviews