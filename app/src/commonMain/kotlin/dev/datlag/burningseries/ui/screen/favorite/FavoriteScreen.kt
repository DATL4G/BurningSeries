package dev.datlag.burningseries.ui.screen.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.datlag.burningseries.LocalStringRes
import dev.datlag.burningseries.common.getValueBlocking
import dev.datlag.burningseries.common.isTv
import dev.datlag.burningseries.ui.screen.home.gridCellSize
import dev.datlag.burningseries.ui.screen.home.series.SeriesItem

@Composable
fun FavoriteScreen(component: FavoriteComponent) {
    val favorites by component.favorites.collectAsState(component.favorites.getValueBlocking(emptyList()))

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        component.onGoBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = LocalStringRes.current.back,
                            tint = MaterialTheme.colorScheme.onTertiary,
                        )
                    }
                },
                title = {
                    Text(
                        text = LocalStringRes.current.favorites,
                        color = MaterialTheme.colorScheme.onTertiary,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true
                    )
                }
            )
        }
    ) {
        if (isTv()) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(favorites) {
                    SeriesItem(it, component)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = gridCellSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) {
                    SeriesItem(it, component)
                }
            }
        }
    }
}