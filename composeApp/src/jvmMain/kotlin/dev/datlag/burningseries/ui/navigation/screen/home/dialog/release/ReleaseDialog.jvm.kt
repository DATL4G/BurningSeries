package dev.datlag.burningseries.ui.navigation.screen.home.dialog.release

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrowserUpdated
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import dev.datlag.burningseries.MokoRes
import dev.datlag.burningseries.other.Constants
import dev.icerock.moko.resources.compose.painterResource

@Composable
actual fun ReleaseDialog(component: ReleaseComponent) {
    val isDraftOrPreRelease = remember(component.release) { component.release.isDraft || component.release.isPrerelease }
    var dismissRequests by remember(isDraftOrPreRelease) { mutableIntStateOf(if (isDraftOrPreRelease) 5 else 0) }

    AlertDialog(
        onDismissRequest = {
            dismissRequests++
            if (dismissRequests >= 5) {
                component.dismiss()
            }
        },
        icon = {
            Icon(
                imageVector = DeviceIcon,
                contentDescription = null
            )
        },
        title = {
            Text(text = component.release.title ?: component.release.tagName)
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "A new version is available ${component.release.tagName}\nYou should check it out!",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            val uriHandler = LocalUriHandler.current

            TextButton(
                onClick = {
                    uriHandler.openUri(component.release.url ?: Constants.GITHUB_RELEASE)
                }
            ) {
                Image(
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    painter = painterResource(MokoRes.images.github),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "View")
            }
        }
    )
}

internal actual val DeviceIcon: ImageVector
    @Composable
    get() = Icons.Rounded.BrowserUpdated