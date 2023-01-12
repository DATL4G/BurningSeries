package dev.datlag.burningseries.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.datlag.burningseries.LocalResources
import dev.datlag.burningseries.LocalStringRes
import dev.datlag.burningseries.common.OnWarning
import dev.datlag.burningseries.common.Warning
import dev.datlag.burningseries.common.getValueBlocking
import dev.datlag.burningseries.network.Status
import dev.datlag.burningseries.other.Constants
import dev.datlag.burningseries.other.EmptyInputStream
import dev.datlag.burningseries.other.Resources
import dev.datlag.burningseries.ui.Shape
import dev.datlag.burningseries.ui.custom.*
import dev.datlag.burningseries.ui.custom.SnackbarData
import dev.datlag.burningseries.ui.dialog.release.NewReleaseComponent
import dev.datlag.burningseries.ui.dialog.release.NewReleaseDialog
import kotlinx.coroutines.launch
import java.io.InputStream
import androidx.compose.material.SnackbarDefaults

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeScreen(component: HomeComponent) {
    val dialogState = component.dialog.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState(
        snackbarHostState = snackbarHostState
    )

    val resources = LocalResources.current
    val strings = LocalStringRes.current

    fun loadGitHubIcon(): InputStream {
        return resources.getResourcesAsInputStream(Resources.GITHUB_ICON) ?: EmptyInputStream
    }

    val _githubIconInput = remember { loadGitHubIcon() }
    val githubIconInput = if (_githubIconInput.available() > 0) _githubIconInput else loadGitHubIcon()
    var snackbarColors by mutableStateOf(SnackbarDefaults.backgroundColor to androidx.compose.material.MaterialTheme.colors.surface)
    val colorScheme = MaterialTheme.colorScheme

    snackbarHandlerForStatus(
        state = snackbarHostState,
        status = component.status,
        mapper = {
            when (it) {
                is Status.LOADING -> strings.loadingHome
                is Status.ERROR.TOO_MANY_REQUESTS -> strings.tooManyRequests
                is Status.ERROR -> strings.errorTryAgain
                else -> null
            }
        }
    ) { status ->
        snackbarColors = when (status) {
            is Status.LOADING -> Color.Warning to Color.OnWarning
            is Status.ERROR -> colorScheme.error to colorScheme.onError
            else -> colorScheme.surface to colorScheme.onSurface
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = {
                        Text(
                            text = strings.appName,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            strings.openInBrowser(Constants.GITHUB_REPOSITORY_URL)
                        }) {
                            SVGImage(
                                stream = githubIconInput,
                                description = strings.githubRepository,
                                scale = ContentScale.Inside,
                                tint = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        OverflowMenu(MaterialTheme.colorScheme.onTertiary) {
                            DropdownMenuItem(onClick = {
                                component.onSettingsClicked()
                            }, enabled = true, text = {
                                Text(
                                    text = strings.settings
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = strings.settings
                                )
                            })
                            DropdownMenuItem(onClick = {
                                component.onAboutClicked()
                            }, enabled = true, text = {
                                Text(
                                    text = strings.about
                                )
                            }, icon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = strings.about
                                )
                            })
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = 0.dp
                )
                RoundTabs(listOf(strings.episodes, strings.seriesPlural), component.childIndex)
            }
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val favoritesExists by component.favoritesExists.collectAsState(component.favoritesExists.getValueBlocking(false))

                if (favoritesExists) {
                    FloatingActionButton(onClick = {
                        component.onFavoritesClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null
                        )
                    }
                }
                FloatingActionButton(onClick = {
                    component.onSearchClicked()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = snackbarColors.first,
                    contentColor = snackbarColors.second,
                    shape = Shape.FullRoundedShape,
                    elevation = 0.dp,
                    actionOnNewLine = false
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard(
                title = strings.beta,
                text = strings.betaText,
                backgroundColor = Color.Warning,
                contentColor = Color.OnWarning,
                icon = Icons.Default.Warning,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
            )
            HomeViewPager(component)
        }
    }

    dialogState.value.overlay?.also { (config, instance) ->
        when (config) {
            is DialogConfig.NewRelease -> {
                NewReleaseDialog(instance as NewReleaseComponent)
            }
        }
    }
}

@Composable
expect fun gridCellSize(): GridCells
