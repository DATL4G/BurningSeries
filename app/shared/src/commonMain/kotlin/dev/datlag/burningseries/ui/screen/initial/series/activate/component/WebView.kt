package dev.datlag.burningseries.ui.screen.initial.series.activate.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebView(url: String, scrapingJs: String, modifier: Modifier = Modifier, onScraped: (String) -> Unit)