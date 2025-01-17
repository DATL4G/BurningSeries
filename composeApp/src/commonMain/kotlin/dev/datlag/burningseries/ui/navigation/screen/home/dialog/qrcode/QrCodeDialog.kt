package dev.datlag.burningseries.ui.navigation.screen.home.dialog.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.datlag.burningseries.composeapp.generated.resources.Res
import dev.datlag.burningseries.composeapp.generated.resources.close
import dev.datlag.nanoid.NanoIdUtils
import dev.icerock.moko.resources.compose.painterResource
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource
import dev.datlag.burningseries.MokoRes
import dev.datlag.burningseries.composeapp.generated.resources.sync_settings
import dev.datlag.burningseries.composeapp.generated.resources.sync_settings_finished
import dev.datlag.burningseries.composeapp.generated.resources.sync_settings_wait
import dev.datlag.burningseries.other.Constants
import dev.datlag.burningseries.ui.custom.TintPainter
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.toImageBitmap

@Composable
fun QrCodeDialog(component: QrCodeComponent) {
    val settingsSynced by component.syncedSettings.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = { },
        icon = {
            if (settingsSynced) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.QrCode,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(text = stringResource(Res.string.sync_settings))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                if (settingsSynced) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.sync_settings_finished),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.sync_settings_wait),
                        textAlign = TextAlign.Center
                    )

                    val contentColor = LocalContentColor.current
                    val logo = painterResource(MokoRes.images.Logo)
                    Image(
                        modifier = Modifier.size(200.dp),
                        painter = rememberQrCodePainter("${Constants.SYNCING_URL}${component.identifier}") {
                            logo {
                                painter = TintPainter(logo, contentColor)
                                padding = QrLogoPadding.Natural(0.1F)
                            }
                            shapes {
                                ball = QrBallShape.circle()
                                frame = QrFrameShape.circle()
                                darkPixel = QrPixelShape.circle()
                            }
                            colors {
                                ball = QrBrush.solid(contentColor)
                                frame = QrBrush.solid(contentColor)
                                dark = QrBrush.solid(contentColor)
                            }
                        },
                        contentDescription = null
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    component.dismiss()
                }
            ) {
                Text(text = stringResource(Res.string.close))
            }
        }
    )
}