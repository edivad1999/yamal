# Missing Features - MyAnimeList API Limitations

This document lists features that cannot be implemented in YAMAL due to limitations in the MyAnimeList API v2.

## Features Not Available via API

| Feature | Reason | Workaround |
|---------|--------|------------|
| **Character Details** | MAL API v2 does not return character data in anime/manga details endpoints. The `characters` field is not available. | None - would require web scraping |
| **Voice Actors** | Character voice actor data is not available in the API response. | None - would require web scraping |
| **Staff Details** | Detailed staff information (director, writer, etc.) is not available in anime details. | None |
| **Reviews (Read)** | No endpoint to fetch reviews for anime/manga. | None |
| **Reviews (Write)** | No endpoint to post reviews. | None |
| **Forum Posts (Read)** | Forum API endpoints exist but are limited to topic listings, not full discussions. | Partial - can list topics |
| **Forum Posts (Write)** | Cannot create forum posts, replies, or comments via API. | None |
| **Friend Activity Feed** | No social feed or activity stream endpoint. | None |
| **Friend List** | Cannot fetch or manage friend list. | None |
| **Push Notifications** | No API for notification preferences or push notification tokens. | None |
| **User Avatar Upload** | Cannot modify user profile or upload avatars. | None |
| **Trailers/Videos** | Video/trailer URLs are not provided by MAL API. | Could integrate YouTube search separately |
| **Episode List Details** | Individual episode titles and air dates are not available. Only total episode count. | None |
| **News** | No news or announcements endpoint. | None |
| **Clubs** | Club membership and club pages are not accessible via API. | None |
| **Recommendations (Write)** | Cannot create or submit recommendations. | None |
| **Interest Stacks** | No endpoint for interest stacks. | None |
| **Watch History** | No detailed watch history beyond list status. | None |
| **Import/Export Data** | Cannot bulk import or export user data. | Manual list management only |

## Partial Feature Support

| Feature | Limitation | What's Available |
|---------|------------|------------------|
| **User Profile** | Limited fields available | Basic user info, anime/manga statistics |
| **Search** | Basic text search only | No advanced filters (year, season, studio, genre) in search endpoint |
| **Sorting** | Limited sort options | Ranking endpoints support some types, list supports basic sorting |
| **Related Anime/Manga** | Available but basic | Relation type and title, no detailed information |
| **Recommendations** | Read-only | Can view recommendations, cannot create |
| **Statistics** | Basic counts only | No detailed viewing trends or history |

## Notes

- The MyAnimeList API v2 is designed primarily for list management, not for comprehensive data access.
- Some features visible on the MAL website use internal APIs that are not publicly documented.
- Rate limiting applies: ~60 requests per minute with a 1000 request daily cap for unauthenticated requests.
- OAuth2 authentication is required for all user-specific operations.

## Alternative Data Sources

For features not available via MAL API, consider:

1. **Jikan API** - Unofficial MAL API with more data (character, staff, episodes)
   - Note: This is web scraping based and may violate MAL ToS

2. **AniList API** - GraphQL API with comprehensive data
   - Different database, may have different titles/IDs

3. **Kitsu API** - Another anime database with good coverage
   - May lack some titles available on MAL

## API Reference

Official documentation: https://myanimelist.net/apiconfig/references/api/v2
