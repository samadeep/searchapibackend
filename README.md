# Search API

A powerful search API that aggregates results from multiple sources including GitHub, StackOverflow, Google, Bing, YouTube, and Confluence.

## Features

- 🔍 Multi-source search aggregation
- ⚡ Real-time search results
- 🔒 Secure authentication
- 📊 Faceted search results
- 🚀 Rate limiting per source
- 📝 OpenAPI documentation
- 🎯 Pagination support
- 🔄 Caching support
- 📈 Health monitoring
- 🛡️ Input validation

## Prerequisites

- Java 21 or higher
- PostgreSQL 14 or higher
- Maven 3.8 or higher

## Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd searchapi
   ```

2. **Database Setup**
   ```bash
   # Create PostgreSQL database
   createdb searchapi
   ```

3. **Build the application**
   ```bash
   ./mvnw clean package -DskipTests
   ```

4. **Run the application**
   ```bash
   java -jar -Dspring.profiles.active=dev target/searchapi-0.0.1-SNAPSHOT.jar
   ```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/searchapi` |
| `DATABASE_USERNAME` | Database username | System username |
| `DATABASE_PASSWORD` | Database password | Empty |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |
| `H2_CONSOLE_ENABLED` | Enable H2 console | `true` (dev) / `false` (prod) |

### Application Properties

The application uses different profiles for development and production:

- `application-dev.properties`: Development settings
- `application-prod.properties`: Production settings
- `application.properties`: Common settings

## API Documentation

Once the application is running, you can access:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

## API Endpoints

### Search API

#### GET /api/search
```bash
curl -X GET "http://localhost:8080/api/search?query=spring+boot&sources=GITHUB,STACKOVERFLOW" \
     -u admin:admin123
```

Parameters:
- `query` (required): Search query string
- `page` (optional): Page number (default: 1)
- `size` (optional): Results per page (default: 10, max: 100)
- `sources` (optional): Comma-separated list of sources
- `facets` (optional): Comma-separated list of facet fields

#### POST /api/search
```bash
curl -X POST "http://localhost:8080/api/search" \
     -H "Content-Type: application/json" \
     -u admin:admin123 \
     -d '{
       "query": "spring boot",
       "page": 1,
       "size": 10,
       "sources": ["GITHUB", "STACKOVERFLOW"],
       "facets": ["language", "type"]
     }'
```

## Development Tools

### H2 Console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:postgresql://localhost:5432/searchapi`
- Username: Your system username
- Password: Empty

### Actuator Endpoints
- Health: http://localhost:8080/actuator/health
- Info: http://localhost:8080/actuator/info
- Metrics: http://localhost:8080/actuator/metrics

## Project Structure

```
src/main/java/com/search/searchapi/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── exceptions/       # Custom exceptions
├── models/          # Domain models
├── repository/      # JPA repositories
├── service/         # Business logic
│   └── impl/       # Service implementations
└── ratelimit/       # Rate limiting implementation
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 