package dev.datlag.burningseries.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import dev.datlag.burningseries.common.backAnimation
import dev.datlag.burningseries.ui.screen.initial.InitialScreenComponent
import dev.datlag.burningseries.ui.screen.video.VideoScreenComponent
import org.kodein.di.DI

class NavHostComponent(
    componentContext: ComponentContext,
    override val di: DI
) : Component, ComponentContext by componentContext {

    private val navigation = StackNavigation<ScreenConfig>()
    private val stack = childStack(
        source = navigation,
        initialConfiguration = ScreenConfig.Home,
        childFactory = ::createScreenComponent
    )

    private fun createScreenComponent(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ) : Component {
        return when (screenConfig) {
            is ScreenConfig.Home -> InitialScreenComponent(
                componentContext = componentContext,
                di = di,
                watchVideo = {
                    navigation.push(ScreenConfig.Video(it.toList()))
                }
            )
            is ScreenConfig.Video -> VideoScreenComponent(
                componentContext = componentContext,
                di = di,
                initialStreams = screenConfig.streams,
                onBack = navigation::pop
            )
        }
    }

    @Composable
    override fun render() {
        Children(
            stack = stack,
            animation = backAnimation(
                backHandler = this.backHandler,
                onBack = {
                    navigation.pop()
                }
            )
        ) {
            it.instance.render()
        }
    }
}