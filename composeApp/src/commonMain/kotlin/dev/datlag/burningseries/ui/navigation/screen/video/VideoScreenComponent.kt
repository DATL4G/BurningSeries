package dev.datlag.burningseries.ui.navigation.screen.video

import androidx.compose.runtime.Composable
import app.cash.sqldelight.coroutines.asFlow
import com.arkivanov.decompose.ComponentContext
import dev.datlag.burningseries.database.BurningSeries
import dev.datlag.burningseries.database.Episode
import dev.datlag.burningseries.database.common.episodeLengthOneShot
import dev.datlag.burningseries.database.common.episodeProgress
import dev.datlag.burningseries.database.common.episodeProgressOneShot
import dev.datlag.burningseries.database.common.insertEpisodeOrIgnore
import dev.datlag.burningseries.database.common.updateLength
import dev.datlag.burningseries.database.common.updateProgress
import dev.datlag.burningseries.model.Series
import dev.datlag.burningseries.network.EpisodeStateMachine
import dev.datlag.burningseries.network.state.EpisodeAction
import dev.datlag.burningseries.network.state.EpisodeState
import dev.datlag.skeo.Stream
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.compose.withMainContext
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.math.max

class VideoScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val series: Series,
    override val episode: Series.Episode,
    override val streams: ImmutableCollection<Stream>,
    private val onBack: () -> Unit,
    private val onNext: (Series.Episode, ImmutableCollection<Stream>) -> Unit
) : VideoComponent, ComponentContext by componentContext {

    private val database by instance<BurningSeries>()
    override val startingPos: Long = max(database.episodeProgressOneShot(episode), 0L)
    override val startingLength: Long = max(database.episodeLengthOneShot(episode), 0L)

    private val episodeState by instance<EpisodeStateMachine>()
    override val nextEpisode: Flow<EpisodeState> = episodeState.state.flowOn(
        context = ioDispatcher()
    )

    init {
        database.insertEpisodeOrIgnore(
            episode = episode,
            series = series,
        )
    }

    @Composable
    override fun render() {
        onRender {
            VideoScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun ended() {
        var episodeIndex = series.episodes.indexOf(episode)
        if (episodeIndex == -1) {
            episodeIndex = series.episodes.indexOfFirst { it.href == episode.href }
        }

        if (episodeIndex > -1) {
            val nextEpisode = series.episodes.toList().getOrNull(episodeIndex + 1)

            if (nextEpisode != null) {
                launchIO {
                    episodeState.dispatch(EpisodeAction.LoadNonSuccess(nextEpisode))
                }
            }
        }
    }

    override fun length(value: Long) {
        if (value > 0) {
            database.updateLength(value, episode)
        }
    }

    override fun progress(value: Long) {
        if (value > 0) {
            database.updateProgress(value, episode)
        }
    }

    override fun next(episode: Series.Episode, streams: ImmutableCollection<Stream>) {
        launchIO {
            episodeState.dispatch(EpisodeAction.Clear)
            withMainContext {
                onNext(episode, streams)
            }
        }
    }
}