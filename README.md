# Demo Migration Spring Batch

This is a demonstration Spring Batch application that processes a CSV file containing famous European authors, fetches their books from the OpenLibrary API, and stores the aggregated data in an H2 database.

## Project Structure

- **Java Version**: 11
- **Spring Boot Version**: 2.7.18
- **Build Tool**: Maven
- **Database**: H2 (embedded)

## Features

1. **CSV Processing**: Reads authors from a CSV file
2. **REST API Integration**: Fetches book data from OpenLibrary API
3. **Batch Processing**: Spring Batch job with Reader, Processor, Writer
4. **Data Persistence**: Stores authors and books in H2 database

## Architecture

```
CSV File (authors.csv)
    ↓
FlatFileItemReader (AuthorCsvReader)
    ↓
ItemProcessor (AuthorItemProcessor) → Calls OpenLibrary API
    ↓
ItemWriter (AuthorItemWriter) → Saves to H2 Database
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher

### Running the Application

```bash
# Clone the repository
git clone https://github.com/Y838477/demo-migration-spring-batch.git
cd demo-migration-spring-batch

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run
```

### Configuration

The application uses the following configuration (in `application.properties`):

- **H2 Database**: File-based database at `~/spring-batch-authors-db`
- **H2 Console**: Available at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:~/spring-batch-authors-db`
  - Username: `sa`
  - Password: (empty)

### Customizing the Input

To use a different CSV file, update the `input.csv.file` property in `application.properties`:

```properties
input.csv.file=file:/path/to/your/authors.csv
```

CSV format:
```csv
name,nationality
Victor Hugo,French
Charles Dickens,English
```

## Database Schema

The application automatically creates the following tables:

- **AUTHORS**: Stores author information
  - `id` (Primary Key)
  - `name` (Unique)
  - `nationality`

- **BOOKS**: Stores book information
  - `id` (Primary Key)
  - `title`
  - `publication_year`
  - `open_library_id`
  - `author_id` (Foreign Key to AUTHORS)

## Migration Path

The following toolchain is a compatible, staged upgrade path. The Java versions shown are the versions selected for this demonstration, not each Spring Boot line's minimum Java requirement.

| Stage | Spring Boot version | Maven version | Java version |
|---|---|---|---|
| Current baseline | [2.7.18](https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/getting-started.html#getting-started.system-requirements) | 3.6.3+ | 11 |
| Upgrade target | [3.5.x](https://docs.spring.io/spring-boot/3.5/system-requirements.html) | 3.6.3+ | 17 |
| Future upgrade | [4.1.x](https://docs.spring.io/spring-boot/system-requirements.html) | 3.6.3+ | 21 |

Spring Boot 2.7 supports Maven 3.5 or later; Spring Boot 3.5 and 4.1 require Maven 3.6.3 or later. Maven 3.6.3+ therefore works across all three stages. Java 17 is the minimum for Spring Boot 3.x and 4.x; Java 21 is a compatible choice for the final stage. Check the linked system requirements when selecting the exact Spring Boot release.

## API Reference

The application uses the [OpenLibrary API](https://openlibrary.org/developers/api):

- Author Search: `GET /search/authors.json?q={name}`
- Author Works: `GET /authors/{key}/works.json`

## License

This project is open source and available under the MIT License.

## Collaboration

This project is a collaboration between Y838477 and co-authors for demonstrating Spring Boot migration strategies.
