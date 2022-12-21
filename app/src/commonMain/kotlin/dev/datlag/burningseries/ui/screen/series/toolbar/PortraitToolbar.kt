package dev.datlag.burningseries.ui.screen.series.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.datlag.burningseries.LocalStringRes
import dev.datlag.burningseries.common.SemiBlack
import dev.datlag.burningseries.model.Cover
import dev.datlag.burningseries.model.Series
import dev.datlag.burningseries.other.DefaultValue
import dev.datlag.burningseries.ui.custom.ArcShape
import dev.datlag.burningseries.ui.custom.CoverImage
import dev.datlag.burningseries.ui.custom.collapsingtoolbar.DefaultCollapsingToolbar
import dev.datlag.burningseries.ui.custom.collapsingtoolbar.rememberCollapsingToolbarScaffoldState
import dev.datlag.burningseries.ui.screen.series.SeriesComponent
import dev.datlag.burningseries.ui.screen.series.SeriesLanguageSeasonButtons
import kotlin.math.abs
import dev.datlag.burningseries.ui.Shape

@Composable
fun PortraitToolbar(
    component: SeriesComponent,
    title: String,
    cover: Cover?,
    languages: List<Series.Language>?,
    seasons: List<Series.Season>?,
    selectedLanguage: String?,
    selectedSeason: Series.Season?,
    seasonText: String?,
    content: LazyListScope.() -> Unit
) {

    val state = rememberCollapsingToolbarScaffoldState()
    val reversedProgress by remember {
        derivedStateOf { (abs(1F - state.toolbarState.progress)) }
    }

    DefaultCollapsingToolbar(
        state = state,
        expandedBody = {
            var titleHeight by remember { mutableStateOf(0) }

            if (cover != null) {
                CoverImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 320.dp)
                        .parallax(ratio = 0.5F)
                        .padding(bottom = with(LocalDensity.current) {
                            titleHeight.toDp() + 16.dp
                        }),
                    cover = cover,
                    description = title,
                    scale = ContentScale.FillWidth,
                    shape = ArcShape(with(LocalDensity.current) {
                        20.dp.toPx()
                    })
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .road(Alignment.TopStart, Alignment.BottomStart)
                    .padding(16.dp).onSizeChanged {
                        titleHeight = it.height
                    },
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = run {
                    val alpha = state.toolbarState.progress
                    if (alpha < 0.7F) {
                        if (alpha < 0.3F) {
                            0F
                        } else {
                            alpha
                        }
                    } else {
                        1F
                    }
                }),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2
            )
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = run {
                    val alpha = reversedProgress
                    if (alpha > 0.7F) {
                        alpha
                    } else {
                        0F
                    }
                }),
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                component.onGoBack()
            }, modifier = Modifier.background(
                color = if (state.toolbarState.progress == 1F) Color.SemiBlack else Color.Black.copy(alpha = state.toolbarState.progress / 10F),
                shape = Shape.FullRoundedShape
            )) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = LocalStringRes.current.back
                )
            }
        },
        actions = {
            IconButton(onClick = {

            }, modifier = Modifier.background(
                    color = if (state.toolbarState.progress == 1F) Color.SemiBlack else Color.Black.copy(alpha = state.toolbarState.progress / 10F),
                shape = Shape.FullRoundedShape
            )) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null
                )
            }
            IconButton(onClick = {

            }, modifier = Modifier.background(
                color = if (state.toolbarState.progress == 1F) Color.SemiBlack else Color.Black.copy(alpha = state.toolbarState.progress / 10F),
                shape = Shape.FullRoundedShape
            )) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null
                )
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    SeriesLanguageSeasonButtons(
                        component,
                        languages,
                        seasons,
                        selectedLanguage,
                        selectedSeason,
                        seasonText
                    )
                }
            }

            content()
        }
    }
}