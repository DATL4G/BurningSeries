package dev.datlag.burningseries.ui.navigation.screen.activate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.datlag.burningseries.composeapp.generated.resources.Res
import dev.datlag.burningseries.composeapp.generated.resources.activate_on_desktop
import dev.datlag.burningseries.composeapp.generated.resources.download
import dev.datlag.burningseries.other.Constants
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun ActivateScreen(component: ActivateComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(fraction = 0.7F),
            text = stringResource(Res.string.activate_on_desktop),
            textAlign = TextAlign.Center
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val uriHandler = LocalUriHandler.current

            FilledTonalIconButton(
                onClick = {
                    component.back()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null
                )
            }
            Button(
                onClick = {
                    uriHandler.openUri(Constants.GITHUB_RELEASE)
                }
            ) {
                Icon(
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    imageVector = Icons.Rounded.Download,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(Res.string.download))
            }
        }
    }
}