package dev.datlag.burningseries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.datlag.burningseries.composeapp.generated.resources.Res
import dev.datlag.burningseries.composeapp.generated.resources.app_name
import dev.datlag.burningseries.module.NetworkModule
import dev.datlag.burningseries.ui.navigation.RootComponent
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.applicationTitle
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner
import dev.datlag.tooling.scopeCatching
import dev.datlag.tooling.systemProperty
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import javax.swing.SwingUtilities

fun main(vararg args: String) {
    val di = DI {
        systemProperty("jpackage.app-version")?.let {
            bindSingleton("APP_VERSION") { it }
        }

        import(NetworkModule.di)
    }

    runWindow(di)
}

@OptIn(ExperimentalDecomposeApi::class)
private fun runWindow(di: DI) {
    val appTitle = runBlocking {
        getString(Res.string.app_name)
    }
    Tooling.applicationTitle(appTitle)

    val windowState = WindowState()
    val lifecycle = LifecycleRegistry()
    val lifecycleOwner = object : LifecycleOwner {
        override val lifecycle: Lifecycle = lifecycle
    }
    val backDispatcher = BackDispatcher()
    val root = runOnUiThread {
        RootComponent(
            componentContext = DefaultComponentContext(
                lifecycle = lifecycle,
                backHandler = backDispatcher
            ),
            di = di
        )
    }

    singleWindowApplication(
        state = windowState,
        title = appTitle,
        exitProcessOnExit = true
    ) {
        LifecycleController(lifecycle, windowState)

        CompositionLocalProvider(
            LocalLifecycleOwner provides lifecycleOwner
        ) {
            App(di) {
                PredictiveBackGestureOverlay(
                    backDispatcher = backDispatcher,
                    backIcon = { progress, _ ->
                        PredictiveBackGestureIcon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            progress = progress,
                            iconTintColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    root.render()
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        val res = scopeCatching(block)
        error = res.exceptionOrNull()
        result = res.getOrNull()
    }

    error?.also { throw it }

    return result as T
}