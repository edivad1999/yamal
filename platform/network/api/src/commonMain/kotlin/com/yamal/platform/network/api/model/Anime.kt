package com.yamal.platform.network.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("main_picture") val mainPictureNetwork: PictureNetwork?,
    @SerialName("alternative_titles") val alternativeTitlesNetwork: AlternativeTitlesNetwork? = null,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("mean") val mean: Float? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("num_list_users") val numListUsers: Int? = null,
    @SerialName("num_scoring_users") val numScoringUsers: Int? = null,
    @SerialName("nsfw") val nsfw: String? = null,
    @SerialName("genres") val genreNetworks: List<GenreNetwork> = emptyList(),
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("my_list_status") val myListStatusNetwork: MyListStatusNetwork? = null,
    @SerialName("num_episodes") val numEpisodes: Int? = null,
    @SerialName("start_season") val startSeason: StartSeason? = null,
    @SerialName("broadcast") val broadcastNetwork: BroadcastNetwork? = null,
    @SerialName("source") val source: String? = null,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("studios") val studios: List<AnimeStudioNetwork> = emptyList(),
)

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
