package com.yamal.shared

import com.yamal.feature.anime.implementation.di.AnimeModule
import com.yamal.feature.anime.ui.animeDetails.di.AnimeDetailsPresentationModule
import com.yamal.feature.anime.ui.animeRanking.di.AnimeRankingPresentationModule
import com.yamal.feature.anime.ui.animeSeasonal.di.AnimeSeasonalPresentationModule
import com.yamal.feature.anime.ui.home.di.HomePresentationModule
import com.yamal.feature.anime.ui.userAnimeList.di.UserAnimeListPresentationModule
import com.yamal.feature.login.implementation.di.LoginModule
import com.yamal.feature.login.ui.di.LoginPresentationModule
import com.yamal.feature.manga.implementation.di.MangaModule
import com.yamal.feature.manga.ui.mangaDetails.di.MangaDetailsPresentationModule
import com.yamal.feature.search.implementation.di.SearchModule
import com.yamal.feature.search.ui.search.di.SearchPresentationModule
import com.yamal.feature.user.implementation.di.UserModule
import com.yamal.feature.user.ui.profile.di.ProfilePresentationModule
import com.yamal.platform.datasource.implementation.di.DataSourceModule
import com.yamal.platform.jikan.implementation.di.JikanNetworkModule
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
            SearchModule(),
            SearchPresentationModule(),
            UserModule(),
            ProfilePresentationModule(),
            MangaModule(),
            MangaDetailsPresentationModule(),
            NetworkModule(),
            JikanNetworkModule(),
            DataSourceModule(),
            PreferencesModule(),
        )
}
