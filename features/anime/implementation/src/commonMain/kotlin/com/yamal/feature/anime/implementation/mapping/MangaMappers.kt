package com.yamal.feature.anime.implementation.mapping

import com.yamal.feature.anime.api.model.MangaForListYamal
import com.yamal.feature.anime.api.model.MediaTypeYamal
import com.yamal.feature.anime.api.model.RelatedItemYamal
import com.yamal.feature.anime.api.model.RelationTypeYamal
import com.yamal.feature.anime.api.model.RelationYamal
import com.yamal.platform.network.api.model.edge.RelatedMangaEdgeMalNetwork
import com.yamal.platform.network.api.model.manga.MangaForListMalNetwork

/**
 * Maps a network manga for list model to the domain manga for list model.
 */
fun MangaForListMalNetwork.toYamal() =
    MangaForListYamal(
        id = id,
        title = title,
        mainPicture = mainPicture?.toYamal(),
        rank = rank,
        members = numListUsers,
        mean = mean,
        mediaType = MediaTypeYamal.fromSerializedValue(mediaType),
        userVote = myListStatus?.score,
        startDate = startDate,
        endDate = endDate,
        numberOfVolumes = numVolumes,
        numberOfChapters = numChapters,
    )

/**
 * Maps a network related manga edge model to the domain related item model.
 */
fun RelatedMangaEdgeMalNetwork.toYamal() =
    RelatedItemYamal(
        node = node.toYamal(),
        relation =
            RelationYamal(
                type = RelationTypeYamal.fromSerializedValue(relationType),
                formatted = relationTypeFormatted ?: "",
            ),
    )
