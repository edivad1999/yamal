package com.yamal.feature.search.implementation

import androidx.paging.PagingSource
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.search.api.SearchRepository
import com.yamal.platform.datasource.api.AnimeDataSource

class SearchRepositoryImpl(
    private val animeDataSource: AnimeDataSource,
) : SearchRepository {
    override fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal> = animeDataSource.searchAnime(query)

    override fun searchManga(query: String): PagingSource<Int, MangaForListYamal> {
        // TODO: Implement manga search via MangaDataSource when available
        throw NotImplementedError("Manga search not yet implemented with Jikan")
    }
}
