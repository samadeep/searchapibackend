# Search API

A comprehensive search API that aggregates results from multiple sources with advanced filtering, faceting, and pagination capabilities.

## Features

- **Multi-source Search**: Search across multiple sources including GitHub, StackOverflow, Google, Bing, YouTube, and Confluence
- **Advanced Filtering**: Apply custom filters to narrow down search results
- **Faceted Search**: Get aggregated counts for different categories/facets
- **Pagination**: Control the number of results per page and navigate through pages
- **Sorting**: Sort results by relevance or other criteria
- **Performance Metrics**: Track search execution time

## API Endpoints

### 1. Simple Search (GET)

```
GET /api/search
```

Query Parameters:
- `query` (required): Search query string
- `page` (optional, default: 1): Page number
- `size` (optional, default: 10): Number of results per page
- `sources` (optional): Comma-separated list of sources to search
- `facets` (optional): Comma-separated list of facets to include

Example:
```
GET /api/search?query=java&page=1&size=10&sources=GITHUB,STACKOVERFLOW&facets=language,type
```

### 2. Advanced Search (POST)

```
POST /api/search
```

Request Body:
```json
{
    "query": "java",
    "page": 1,
    "size": 10,
    "sources": ["GITHUB", "STACKOVERFLOW"],
    "filters": {
        "language": ["java", "python"],
        "type": ["question", "answer"]
    },
    "facets": ["language", "type"],
    "sortBy": "score",
    "sortOrder": "desc"
}
```

## Response Format

```json
{
    "items": [
        {
            "id": "unique-id",
            "title": "Result Title",
            "description": "Result Description",
            "url": "https://example.com",
            "source": "GITHUB",
            "score": 0.95,
            "metadata": {
                "language": "java",
                "stars": 1000
            }
        }
    ],
    "totalResults": 100,
    "currentPage": 1,
    "totalPages": 10,
    "facets": {
        "language": {
            "java": 50,
            "python": 30
        },
        "type": {
            "question": 40,
            "answer": 60
        }
    },
    "executionTime": 150
}
```

## Error Handling

The API returns appropriate HTTP status codes:
- 200: Successful search
- 400: Bad request (e.g., empty query)
- 500: Internal server error

## Implementation Details

The search API is built using:
- Spring Boot
- Java 21
- Spring Web
- Spring Data JPA
- H2 Database (for development)

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Configuration

The application can be configured through `application.properties`:
- Server port
- Authentication credentials
- Database settings
- Source-specific configurations

## Future Enhancements

- [ ] Implement actual search logic for each source
- [ ] Add caching layer
- [ ] Implement rate limiting
- [ ] Add authentication and authorization
- [ ] Add comprehensive logging
- [ ] Add monitoring and metrics
- [ ] Add test coverage 