package dev.datlag.burningseries.shared.ui.custom

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.burningseries.shared.common.launchMain
import dev.datlag.burningseries.shared.common.withIOContext
import dev.datlag.burningseries.shared.ui.custom.toolbar.*
import kotlinx.coroutines.delay
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultCollapsingToolbar(
    state: CollapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState(),
    expandedBody: @Composable CollapsingToolbarScope.(CollapsingToolbarScaffoldState) -> Unit,
    title: @Composable (CollapsingToolbarScaffoldState) -> Unit,
    navigationIcon: (@Composable (CollapsingToolbarScaffoldState) -> Unit)? = null,
    actions: @Composable RowScope.(CollapsingToolbarScaffoldState) -> Unit = { },
    content: @Composable CollapsingToolbarScaffoldScope.() -> Unit
) {
    val reversedProgress by remember {
        derivedStateOf { (abs(1F - state.toolbarState.progress)) }
    }
    var expanded by remember { mutableStateOf(false) }

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbarModifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp).verticalScroll(rememberScrollState()),
        toolbarScrollable = true,
        toolbar = {
            expandedBody(state)

            TopAppBar(
                modifier = Modifier.pin(),
                backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = reversedProgress),
                contentColor = MaterialTheme.colorScheme.onSurface,
                title = {
                    title(state)
                },
                navigationIcon = if (navigationIcon == null) null else { { navigationIcon(state) } },
                actions = {
                    actions(state)
                },
                elevation = 0.dp
            )
        }
    ) {
        content()
    }

    if (!expanded) {
        rememberCoroutineScope().launchMain {
            state.toolbarState.expand(0)
            repeat(5) {
                withIOContext {
                    delay(20)
                }
                state.toolbarState.expand(0)
            }
            expanded = true
        }
    }
}