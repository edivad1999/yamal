package com.yamal.platform.network.api.model.anime

/**
 * Enum representing available fields that can be requested from the MAL API for anime.
 */
enum class AnimeRequestField(
    val serializableValue: String,
) {
    Mean("mean"),
    NumScoringUsers("num_scoring_users"),
    MyListStatus("my_list_status"),
    MediaType("media_type"),
    NumListUsers("num_list_users"),
    StartDate("start_date"),
    EndDate("end_date"),
    NumberOfEpisodes("num_episodes"),
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
    StartSeason("start_season"),
    Broadcast("broadcast"),
    Source("source"),
    AverageEpisodeDuration("average_episode_duration"),
    Rating("rating"),
    Studios("studios"),
    Pictures("pictures"),
    Background("background"),
    RelatedAnime("related_anime"),
    RelatedManga("related_manga"),
    Recommendations("recommendations"),
    Statistics("statistics"),
    ;

    companion object {
        fun animeRankingFields(): List<AnimeRequestField> =
            listOf(
                Mean,
                NumListUsers,
                MyListStatus,
                MediaType,
                StartDate,
                EndDate,
                NumberOfEpisodes,
            )

        fun animeDetailsFields(): List<AnimeRequestField> =
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
                NumberOfEpisodes,
                StartSeason,
                Broadcast,
                Source,
                AverageEpisodeDuration,
                Rating,
                Studios,
                Pictures,
                Background,
                RelatedAnime,
                RelatedManga,
                Recommendations,
                Statistics,
            )
    }
}

fun List<AnimeRequestField>.mergeToRequestString() = joinToString(",") { it.serializableValue }
