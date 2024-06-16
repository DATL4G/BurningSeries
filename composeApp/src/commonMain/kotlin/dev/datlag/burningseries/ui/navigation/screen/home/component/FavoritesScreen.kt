package dev.datlag.burningseries.ui.navigation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import dev.datlag.burningseries.LocalHaze
import dev.datlag.burningseries.common.fullRow
import dev.datlag.burningseries.common.merge
import dev.datlag.burningseries.ui.navigation.screen.component.HomeCard
import dev.datlag.burningseries.ui.navigation.screen.home.HomeComponent
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoritesScreen(
    padding: PaddingValues,
    component: HomeComponent
) {
    val favorites by component.favorites.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = listState,
            modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
            columns = GridCells.FixedSize(200.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = padding.merge(16.dp)
        ) {
            fullRow {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(favorites.toImmutableList(), key = { it.source }) {
                HomeCard(
                    series = it,
                    modifier = Modifier
                        .width(200.dp)
                        .height(280.dp),
                    onClick = component::details
                )
            }
        }
    }
}