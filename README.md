# UserService
-> It is microservice based application with entity user, address.

-> Implement Spring security with JWT stateless loken.

-> Syemmetnric Type JWT token. Single private key use all other microservices.

-> Folder Structure:
└── 📁src
    └── 📁main
        └── 📁java
            └── 📁com
                └── 📁microservice
                    └── 📁user_service
                        └── 📁common
                            └── 📁component
                                ├── AuditorAwareImpl.java
                            └── 📁config
                                ├── MapperConfig.java
                                ├── SwaggerConfig.java
                            └── 📁constants
                                ├── AppConstant.java
                            └── 📁dto
                                ├── AddressDto.java
                                ├── AddressRequestDto.java
                                ├── ApiResponse.java
                                ├── UserDto.java
                                ├── UserRequestDto.java
                            └── 📁entity
                                ├── BaseEntity.java
                            └── 📁enums
                                ├── Role.java
                                ├── UserStatus.java
                            └── 📁mapper
                                ├── UserMapper.java
                        └── 📁modules
                            └── 📁address
                                └── 📁controller
                                └── 📁entity
                                    ├── Address.java
                                └── 📁repository
                                └── 📁service
                                └── 📁serviceImp
                            └── 📁user
                                └── 📁controller
                                    ├── UserController.java
                                └── 📁entity
                                    ├── User.java
                                └── 📁repository
                                    ├── UserRepository.java
                                └── 📁service
                                    ├── UserService.java
                                └── 📁serviceImp
                                    ├── UserServiceImp.java
                        ├── UserServiceApplication.java
        └── 📁resources
            └── 📁static
            └── 📁templates
            ├── application.properties
    └── 📁test
        └── 📁java
            └── 📁com
                └── 📁microservice
                    └── 📁user_service
                        └── UserServiceApplicationTests.java
