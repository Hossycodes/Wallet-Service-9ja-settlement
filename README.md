# Wallet Service

Simple wallet management service built with Spring Boot. Provides APIs to create wallets, deposit, withdraw, and transfer funds. Responses are wrapped with a generic `ApiResponse<T>`.

## Features

- Create wallets
- Get wallet details
- Deposit funds (CREDIT transaction)
- Withdraw funds (DEBIT transaction)
- Transfer funds between wallets (TRANSFER_OUT / TRANSFER_IN)
- OpenAPI/Swagger annotations for API documentation
- Request/response DTOs and validation

## Tech Stack

- Java (Spring Boot)
- Maven
- Jakarta Validation
- Lombok
- OpenAPI / Swagger (io.swagger.v3)
- macOS development environment (recommended)

## Repository Layout (key files)

- `src/main/java/com/_jasettlement/Wallet/controller/WalletController.java` — REST controller exposing wallet APIs
- `src/main/java/com/_jasettlement/Wallet/service/WalletService.java` — service layer (business logic)
- `src/main/java/com/_jasettlement/Wallet/dto/request` — request DTOs (`CreateWalletRequest`, `DepositRequest`, `WithdrawalRequest`, `TransferRequest`)
- `src/main/java/com/_jasettlement/Wallet/dto/response` — response DTOs (`WalletResponse`, `TransactionResponse`, `ApiResponse`)
- `pom.xml` — Maven build file
- `src/main/resources/application.yml` or `application.properties` — configuration

## Requirements

- Java 17+ (or configured project JDK)
- Maven 3.6+
- Lombok plugin enabled in IDE
- Internet access to download dependencies

## Configuration

1. Review `src/main/resources/application.yml` (or `application.properties`) and set any environment-specific values (database, ports, etc.).
2. If using an external DB, configure datasource properties accordingly.

## Build & Run

From project root on macOS:

- Using Maven wrapper:
  - `./mvnw clean package`
  - `./mvnw spring-boot:run`
- Or Maven installed:
  - `mvn clean package`
  - `mvn spring-boot:run`

Run from IntelliJ IDEA: open the project and run the main Spring Boot application class.

## API (overview)

Base path: `/api/v1/wallets`

- Create wallet
  - Method: `POST`
  - Path: `/api/v1/wallets`
  - Request: `CreateWalletRequest` (JSON)
  - Response: `201` -> `ApiResponse<WalletResponse>`

- Get wallet by ID
  - Method: `GET`
  - Path: `/api/v1/wallets/{walletId}`
  - Response: `200` -> `ApiResponse<WalletResponse>`

- Deposit
  - Method: `POST`
  - Path: `/api/v1/wallets/deposit`
  - Request: `DepositRequest`
  - Response: `200` -> `ApiResponse<TransactionResponse>`

- Withdraw
  - Method: `POST`
  - Path: `/api/v1/wallets/withdraw`
  - Request: `WithdrawalRequest`
  - Response: `200` -> `ApiResponse<TransactionResponse>`

- Transfer
  - Method: `POST`
  - Path: `/api/v1/wallets/transfer`
  - Request: `TransferRequest`
  - Response: `200` -> `ApiResponse<TransactionResponse>`

Errors commonly return `ApiResponse` with `success: false` and an explanatory `message`. Standard HTTP codes used: `200`, `201`, `400`, `404`, etc.

## DTOs & Response Wrapper

- `ApiResponse<T>` is a generic wrapper with:
  - `success` (boolean)
  - `data` (generic payload)
  - `message` (error or informational)

Use this wrapper for all controller responses to provide consistent API structure.

## API Documentation

OpenAPI annotations are present in controllers. If configured, Swagger UI is typically available at:

- `http://localhost:8080/swagger-ui.html` or `http://localhost:8080/swagger-ui/index.html`

Confirm the exact path by checking your OpenAPI/Springdoc configuration.

## Testing

- Unit tests: `mvn test`
- For integration tests, configure test profile and database (in-memory H2 recommended).

## Development Notes

- Ensure Lombok is enabled in the IDE to avoid missing generated methods.
- Controller methods use `@Valid` for request validation; provide proper validation annotations in request DTOs.
- Use `ResponseEntity<ApiResponse<...>>` to control status codes and consistent response shape.
- Avoid class name clashes with Swagger annotations by using fully-qualified names if needed.

## Troubleshooting

- If swagger annotations clash, use fully-qualified `io.swagger.v3.oas.annotations.responses.ApiResponse` in imports.
- If build fails due to missing Lombok, add Lombok plugin and enable annotation processing.

## Contributing

- Fork the repo, create a feature branch, add tests, and submit a pull request.
- Follow existing package structure and naming conventions.

## License

Specify license in `LICENSE` file (if any) and update `pom.xml` metadata accordingly.
