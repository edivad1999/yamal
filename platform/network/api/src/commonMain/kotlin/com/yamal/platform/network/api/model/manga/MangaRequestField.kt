package com.yamal.platform.network.api.model.manga

/**
 * Enum representing available fields that can be requested from the MAL API for manga.
 */
enum class MangaRequestField(
    val serializableValue: String,
) {
    Mean("mean"),
    NumScoringUsers("num_scoring_users"),
    MyListStatus("my_list_status"),
    MediaType("media_type"),
    NumListUsers("num_list_users"),
    StartDate("start_date"),
    EndDate("end_date"),
    NumVolumes("num_volumes"),
    NumChapters("num_chapters"),
    Id("id"),
    Title("title"),
    MainPicture("main_picture"),
    AlternativeTitles("alternative_titles"),
    Synopsis("synopsis"),
    Rank("rank"),
    Popularity("popularity"),
    Nsfw("nsfw"),
    Genres("genres"),
    CreatedAt("created_at"),
    UpdatedAt("updated_at"),
    Status("status"),
    Authors("authors"),
    Pictures("pictures"),
    Background("background"),
    RelatedAnime("related_anime"),
    RelatedManga("related_manga"),
    Recommendations("recommendations"),
    Serialization("serialization"),
    ;

    companion object {
        fun mangaRankingFields(): List<MangaRequestField> =
            listOf(
                Mean,
                NumListUsers,
                MyListStatus,
                MediaType,
                StartDate,
                EndDate,
                NumVolumes,
                NumChapters,
            )

        fun mangaDetailsFields(): List<MangaRequestField> =
            listOf(
                Id,
                Title,
                MainPicture,
                AlternativeTitles,
                StartDate,
                EndDate,
                Synopsis,
                Mean,
                Rank,
                Popularity,
                NumListUsers,
                Nsfw,
                Genres,
                CreatedAt,
                UpdatedAt,
                MediaType,
                Status,
                MyListStatus,
                NumVolumes,
                NumChapters,
                Authors,
                Pictures,
                Background,
                RelatedAnime,
                RelatedManga,
                Recommendations,
                Serialization,
            )
    }
}

fun List<MangaRequestField>.mergeToRequestString() = joinToString(",") { it.serializableValue }
