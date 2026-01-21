package com.yamal.feature.anime.api.model

enum class RelationTypeYamal(
    val serialName: String,
) {
    Sequel("sequel"),
    Prequel("prequel"),
    AlternativeSetting("alternative_setting"),
    AlternativeVersion("alternative_version"),
    SideStory("side_story"),
    ParentStory("parent_story"),
    Summary("summary"),
    FullStory("full_story"),
    ;

    companion object {
        fun fromSerializedValue(value: String): RelationTypeYamal = entries.first { it.serialName == value }
    }
}
