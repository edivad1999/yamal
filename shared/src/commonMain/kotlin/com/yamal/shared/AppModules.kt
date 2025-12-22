package com.yamal.shared

import com.yamal.feature.anime.implementation.di.AnimeModule
import com.yamal.feature.anime.ui.animeDetails.di.AnimeDetailsPresentationModule
import com.yamal.feature.anime.ui.animeRanking.di.AnimeRankingPresentationModule
import com.yamal.feature.anime.ui.animeSeasonal.di.AnimeSeasonalPresentationModule
import com.yamal.feature.anime.ui.home.di.HomePresentationModule
import com.yamal.feature.anime.ui.userAnimeList.di.UserAnimeListPresentationModule
import com.yamal.feature.login.implementation.di.LoginModule
import com.yamal.feature.login.ui.di.LoginPresentationModule
import com.yamal.platform.network.implementation.di.NetworkModule
import com.yamal.platform.storage.implementation.di.PreferencesModule

object AppModules {

    fun exportModules() =
        listOf(
            HomePresentationModule(),
            AnimeRankingPresentationModule(),
            AnimeSeasonalPresentationModule(),
            UserAnimeListPresentationModule(),
            AnimeDetailsPresentationModule(),
            LoginModule(),
            AnimeModule(),
            LoginPresentationModule(),
            NetworkModule(),
            PreferencesModule(),
        )
}
