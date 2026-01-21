package com.yamal.feature.anime.api.model

data class RelatedItemYamal<T>(
    val node: T,
    val relation: RelationYamal,
)

data class RelationYamal(
    val type: RelationTypeYamal,
    val formatted: String,
)
