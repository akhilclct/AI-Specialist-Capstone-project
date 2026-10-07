# E-Commerce API - Spring Boot Application

A comprehensive RESTful API for an e-commerce application built with Spring Boot 3.2.0, Java 21, and PostgreSQL.

## Features

- **User Authentication & Authorization** - JWT-based authentication
- **Product Catalog Management** - Categories, brands, and products with images
- **Shopping Cart** - Add, update, and remove items
- **Order Management** - Place orders, view order history, and buy again functionality
- **Payment Processing** - Support for credit/debit card payments
- **Address Management** - Multiple delivery addresses with default selection
- **Gift Points System** - Earn and redeem gift points
- **Related Products & Recommendations** - Product suggestions based on categories

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Database Migration**: Flyway
- **Build Tool**: Maven
- **API Documentation**: SpringDoc OpenAPI (Swagger)

## Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 14+
- Git

## Database Setup

1. Install PostgreSQL and create a database:

```sql
CREATE DATABASE ecommerce_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ecommerce_db TO postgres;
```

2. Update database credentials in `src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce_db
    username: postgres
    password: postgres
```

## Installation & Running

### 1. Clone the repository

```bash
cd ecommerce-api
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/v1`

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/v1/swagger-ui.html
```

API documentation in JSON format:

```
http://localhost:8080/v1/api-docs
```

## Project Structure

```
ecommerce-api/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── repository/          # Spring Data JPA repositories
│   │   │   ├── service/             # Business logic layer
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── security/            # Security configuration & JWT
│   │   │   ├── exception/           # Custom exceptions
│   │   │   └── EcommerceApplication.java
│   │   └── resources/
│   │       ├── application.yml      # Application configuration
│   │       └── db/migration/        # Flyway migration scripts
│   └── test/                        # Test classes
├── pom.xml
└── README.md
```

## Database Schema

The application uses the following main entities:

- **Users** - Customer accounts
- **Categories** - Product categories
- **Brands** - Product brands
- **Products** - Product catalog
- **Product Images** - Product image URLs
- **Carts** - Shopping carts
- **Cart Items** - Items in shopping carts
- **Addresses** - Delivery addresses
- **Orders** - Customer orders
- **Order Items** - Items in orders
- **Payments** - Payment transactions
- **Gift Point Accounts** - User gift point balances
- **Gift Point Transactions** - Gift point transaction history

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login

### Users
- `GET /users/me` - Get current user profile
- `PUT /users/me` - Update user profile

### Categories
- `GET /categories` - List all categories
- `GET /categories/{categoryId}` - Get category by ID

### Brands
- `GET /brands` - List all brands
- `GET /brands/{brandId}` - Get brand by ID

### Products
- `GET /products` - List products (with filters)
- `GET /products/{productId}` - Get product details
- `GET /products/{productId}/related` - Get related products
- `GET /products/{productId}/images` - Get product images

### Shopping Cart
- `GET /cart` - Get current cart
- `POST /cart/items` - Add item to cart
- `PUT /cart/items/{cartItemId}` - Update cart item quantity
- `DELETE /cart/items/{cartItemId}` - Remove item from cart
- `DELETE /cart/clear` - Clear entire cart
- `GET /cart/recommendations` - Get product recommendations

### Addresses
- `GET /addresses` - List user addresses
- `POST /addresses` - Create new address
- `GET /addresses/{addressId}` - Get address by ID
- `PUT /addresses/{addressId}` - Update address
- `DELETE /addresses/{addressId}` - Delete address
- `PUT /addresses/{addressId}/set-default` - Set default address

### Orders
- `GET /orders` - List user orders
- `POST /orders` - Create new order
- `GET /orders/{orderId}` - Get order details
- `GET /orders/{orderId}/items` - Get order items
- `POST /orders/{orderId}/buy-again` - Reorder items

### Payments
- `POST /payments` - Initiate payment
- `GET /payments/{paymentId}` - Get payment details
- `POST /payments/{paymentId}/complete` - Complete payment
- `GET /orders/{orderId}/payments` - Get order payments

### Gift Points
- `GET /gift-points/account` - Get gift point balance
- `GET /gift-points/transactions` - List transactions
- `POST /gift-points/redeem` - Redeem gift points

## Configuration

### JWT Configuration

Update JWT settings in `application.yml`:

```yaml
jwt:
  secret: your-256-bit-secret-key-change-this-in-production
  expiration: 86400000  # 24 hours
  issuer: ecommerce-api
```

**Important**: Change the JWT secret in production!

### Database Migration

Flyway automatically runs database migrations on startup. Migration scripts are located in:

```
src/main/resources/db/migration/
```

## Security

- All endpoints except `/auth/register` and `/auth/login` require JWT authentication
- Include JWT token in the Authorization header: `Bearer <token>`
- Passwords are hashed using BCrypt

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package -DskipTests
java -jar target/ecommerce-api-1.0.0.jar
```

## Environment Variables

For production deployment, set these environment variables:

```bash
export JWT_SECRET=your-production-secret-key
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/ecommerce_db
export SPRING_DATASOURCE_USERNAME=your-db-username
export SPRING_DATASOURCE_PASSWORD=your-db-password
```

## Logging

Logs are configured in `application.yml`. Default log level is INFO for production and DEBUG for development.

## Error Handling

The API returns standardized error responses:

```json
{
  "code": "ERROR_CODE",
  "message": "Error description",
  "details": [],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## Support

For issues and questions, please create an issue in the repository.

## Next Steps

To complete the implementation, you need to:

1. **Implement Service Layer** - Create service classes for business logic
2. **Implement Controllers** - Create REST controllers for all endpoints
3. **Implement DTOs** - Create Data Transfer Objects for API requests/responses
4. **Implement Security Configuration** - Complete JWT filter and security config
5. **Implement Exception Handling** - Create global exception handler
6. **Add Validation** - Add input validation annotations
7. **Write Tests** - Unit and integration tests
8. **Add Sample Data** - Create seed data for testing

## Sample Service Implementation

Here's an example of how to implement a service:

```java
@Service
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
```

## Sample Controller Implementation

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);
        // Map to DTO and return
        return ResponseEntity.ok(products.map(this::toDTO));
    }
}
```

---

**Built with ❤️ using Spring Boot and Java 21**