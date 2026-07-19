# archetype-java-service
A robust and scalable Java microservice archetype built with Spring Boot, designed to provide a solid foundation for new projects. It incorporates best practices for development, testing, and deployment, including a layered architecture, comprehensive error handling, and containerization with Docker.

# Version
![version](https://img.shields.io/badge/version-1.0.0-blue.svg)

For more details, see the [CHANGELOG](CHANGELOG) file.

# Quality Gate

[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=felipemonzon_archetype-java-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=felipemonzon_archetype-java-service)

[![CI/CD Main](https://github.com/felipemonzon/archetype-java-service/actions/workflows/main.yml/badge.svg)](https://github.com/felipemonzon/archetype-java-service/actions/workflows/main.yml)

### 🛠️ Prerequisites
Ensure you have the following installed before running the project:

* IntelliJ
* Gradle
* Java
* MySQL
* Docker

### 📁 Folder Structure Diagram:

```
├── .github/
│   └── workflows/
│       └── main.yml
│       └── PR main.yml
├── docker/
│   └── Dockerfile
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/moontech/archetype/
│   │   │       ├── application/  (Business logic, mappers, services)
│   │   │       ├── commons/      (Constants, enums)
│   │   │       ├── domain/       (Entities, repositories)
│   │   │       ├── infrastructure/ (Controllers, security, exceptions, models)
│   │   │       └── ArchetypeApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-test.properties
│   │       └── db/migration/
│   └── test/
│       └── java/
│           └── com/moontech/archetype/
│               ├── configuration/ (Testcontainers setup)
│               ├── constants/     (Test constants)
│               └── controller/    (Unit tests for controllers)
├── .gitignore
├── build.gradle
├── docker-compose.yml
├── gradlew
├── gradlew.bat
├── HELP.md
├── LICENSE
├── README.md
├── settings.gradle
└── ... (other config files)
```

### 🔐 Decrypt and Encrypt data with jasypt

To decrypt the data in the properties file, it is necessary to add this parameter when starting the project:

Before running the project, add the following environment variable to your system:
Note: The password is the same as the one used to encrypt the data, change it to your own.
```
export JASYPT_ENCRYPTOR_PASSWORD=felipemonzon  
```

In your application.yaml file, you can encrypt sensitive data using the following format:

```
spring:
  datasource:
    username: ENCRYPT(exampleEncryptedUsername)
```

After that, you can run this command to encrypt sensitive data in the properties file:
```
./gradlew encryptProperties --password=felipemonzon
```

To encrypt sensitive data, it is necessary to compile with the following instruction:
```
./gradlew decryptProperties --password=felipemonzon
```

After that, you can run this command to decrypt sensitive data in the properties file:

```
./gradlew decryptProperties --password=felipemonzon
```

you can see the encrypted data in the application.yaml file.

```
spring:
  datasource:
    username: ENC(EncryptedUsername)
```

### 🚀 Execution with Docker Compose 
To simplify local development and testing, a multi-container environment is configured via the docker-compose.yml file. This sets up the microservice along with its database dependency.

Steps to Start the Environment:
Build the application executable:

```
./gradlew clean build -x test
```

Spin up the containers:

```
docker compose up -d --build
```

Note: The -d flag runs containers in background mode, and --build ensures the Docker image is recreated with your latest local code changes.

Check the service status:

```
docker compose ps
```

Stop the environment:

```
docker compose down
```

## 🧪 Testing
Run the complete test suite (unit and integration tests) using Gradle:

```
./gradlew clean test --info
```

## 🛠️ Built With 🛠️

* Spring Boot 4.0.6
* Java 26
* Gradle
* IntelliJ IDEA

### 📋 Code Syntax Formatter

To format the code, do the following:

```
./gradlew spotlessApply
```

To validate the code format, do the following:

```
./gradlew spotlessCheck
```

## 📌 Versioning

We use [GitHub](https://github.com/felipemonzon/archetype-java-service) for versioning.

## ✒️ Authors

* **[Felipe Monzón](https://felipemonzon.github.io/)** - *Fullstack developer*

## 🖇 Contributing


## 📄 License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE) file for details.