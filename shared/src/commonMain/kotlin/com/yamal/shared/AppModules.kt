package com.yamal.shared

import com.yamal.feature.anime.implementation.di.AnimeModule
import com.yamal.feature.login.implementation.di.LoginModule
import com.yamal.feature.network.implementation.di.NetworkModule
import com.yamal.feature.preferences.implementation.di.PreferencesModule
import com.yamal.presentation.animeDetails.di.AnimeDetailsPresentationModule
import com.yamal.presentation.animeRanking.di.AnimeRankingPresentationModule
import com.yamal.presentation.animeSeasonal.di.AnimeSeasonalPresentationModule
import com.yamal.presentation.home.di.HomePresentationModule
import com.yamal.presentation.login.di.LoginPresentationModule
import com.yamal.presentation.userAnimeList.di.UserAnimeListPresentationModule

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
