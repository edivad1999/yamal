package com.yamal.feature.anime.implementation.mapping

import com.yamal.feature.anime.api.model.AnimeMainPicture
import com.yamal.feature.network.api.model.Picture

fun Picture.toModel() = AnimeMainPicture(large = large, medium = medium)
