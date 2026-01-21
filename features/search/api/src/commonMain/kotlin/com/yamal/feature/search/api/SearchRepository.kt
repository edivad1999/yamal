package com.yamal.feature.search.api

import androidx.paging.PagingSource
import com.yamal.feature.anime.api.model.AnimeForListYamal
import com.yamal.feature.anime.api.model.MangaForListYamal

interface SearchRepository {
    fun searchAnime(query: String): PagingSource<Int, AnimeForListYamal>

    fun searchManga(query: String): PagingSource<Int, MangaForListYamal>
}
