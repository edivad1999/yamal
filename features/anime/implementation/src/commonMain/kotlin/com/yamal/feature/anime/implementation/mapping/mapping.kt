package com.yamal.feature.anime.implementation.mapping

import com.yamal.feature.anime.api.model.Picture
import com.yamal.platform.network.api.model.PictureNetwork

fun PictureNetwork.toModel() = Picture(large = large, medium = medium)
