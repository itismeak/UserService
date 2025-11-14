# UserService
-> It is microservice based application with entity user, address.

-> Implement Spring security with JWT stateless loken.

-> Syemmetnric Type JWT token. Single private key use all other microservices.

-> Folder Structure:
src/
 ├─ main/
 │   ├─ java/com/microservice/user_service/
 │   │   ├─ common/
 │   │   │   ├─ component/     # Utility components (e.g., AuditorAwareImpl)
 │   │   │   ├─ config/        # Application configurations (Swagger, Mapper)
 │   │   │   ├─ constants/     # Application constants
 │   │   │   ├─ dto/           # Data Transfer Objects
 │   │   │   ├─ entity/        # Base entity class
 │   │   │   ├─ enums/         # Role, UserStatus enums
 │   │   │   └─ mapper/        # Mapper interfaces (UserMapper)
 │   │   └─ modules/
 │   │       ├─ address/
 │   │       │   ├─ controller/
 │   │       │   ├─ entity/    # Address entity
 │   │       │   ├─ repository/
 │   │       │   ├─ service/
 │   │       │   └─ serviceImp/
 │   │       └─ user/
 │   │           ├─ controller/    # UserController
 │   │           ├─ entity/        # User entity
 │   │           ├─ repository/    # UserRepository
 │   │           ├─ service/       # UserService interface
 │   │           └─ serviceImp/    # UserServiceImp implementation
 │   │
 │   └─ UserServiceApplication.java  # Main Spring Boot application
 │
 ├─ resources/
 │   ├─ static/
 │   ├─ templates/
 │   └─ application.properties
 │
 └─ test/
     └─ java/com/microservice/user_service/
         └─ UserServiceApplicationTests.java
