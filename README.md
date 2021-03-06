
# Parrot task tracker

The project is homework for the course ["Asynchronous architecture"](https://education.borshev.com/architecture).

## Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- Docker & docker-compose

## Quick start

1. Collect the build tools

```bash
project dir$ cd build-tools; mvn clean install 
```

2. Compile the software

```bash
project dir$ mvn clean install <-Dskiptests>
```

`-DskipTests` will cause integration tests skip.


3. Deploy the system

```bash
./deploy.sh up
```

4. Open the web UI: [http://localhost](http://localhost)


# Overview

## Project structure

### [Build tools](build-tools)

The common Maven infrastructure, by which the system is compiled and assembled. Consists of a set of parent POM Maven descriptors, which serve as a build templates for all other system components.

### [Docker images](docker-images)

Some complementay service-unrelated docker images, necessary for the system operation. Currently, it has only the RabbitMQ image with predefined message topology.

### [Parrot commons](parrot-commons)

Consists of some Maven modules, shared between the services.
- `commons-config` is some common Spring Boot configuration shared between the services. Currently it contains common Jackson configuration for serializing/deserializing AMQP messages and HTTP requests/responses.
- `spring-jdbc-config` is a common configuration of Postgres RDBMS for the services. It uses HikaryCP connection pool, configured with FlywayDB for Schema versioning and update (schemas are expected at `classpath:/db/migration`) for each service.
- `events-config` is a common configuration of messaging by means of RabbitMQ between the services. It also provides implementation for transactional outbox, implemented over PostgreSQL.
- `auth-common` is a common configuration for authentication/authorization by means of Spring Security and JWT.

### [Authentication service](parrot-auth)

Authentication subsystem, responsible for managing users, their roles, authenticating them into the system and issuing JWT tokens.

### [Tasks service](parrot-tasks)

The service, responcible for managing the tasks, their status and assignments

### [Accounting service](parrot-accounting)

Billing and accounting service

### [Analytics service](parrot-analytics)

TODO: analytics service

### [Parrot WEB UI](parrot-webui)

Frontend component of the system, contains all static HTML/CSS/JS resources, plus NGinx gateway service, that hosts them.

### [Parrot ITests](parrot-itests)

A set of integration tests for the system.

## Architecture

![PlantUML diagramm](https://www.planttext.com/api/plantuml/img/LL1B2eCm5Dpd5C5zF868oKO4BMt9ejlN6AoeIKcU5M_V14N8QWRcypwLHt2uBZCXeD0wUWVdB1AYxFArHXkasG2eDzWpMXItkhczofA2ftNYWHK_p6lt6vNEeimJ6S5FFj5b6bVAhWOJXqWT1kOTHvMsj8bdqTx3eDdRsKcxkE-J2z78xd6it8NAupxAUhiPLiOhQie2uIVA4yM9OGAdHPwNCIql9YxMNenKslJXbNy0)

## Authentication/Authorization

Authentication is done by means of JWT tokens. The side that issues the tokens is `auth-service`. Tokens secret and expiration times are provided
by means of Spring Configs, with default values of "parrot" and "1 day" correspondingly.
JWT secret is shared among the services in the system, allowing them to authorize incoming requests by validating and parsing the token, issued by `auth-service`.
JWT token uses the user's UUID as a token's subject. Also, JWT token has "roles" claim, with user roles inside.

Authentication and authorization are implemented in [auth-common](parrot-commons/auth-common) module as two Spring Security configurations.
The module is shared among the services as a usual Jar dependency. It contains two configurations:

- `com.skapral.parrot.auth.common.security.AuthenticationConfig` is used by `auth-service` and provides means for issuing JWT token in response to correctly provided
user credentials.
-`com.skapral.parrot.auth.common.security.SecurityConfig` is used by all the services, including `auth-service`. It is used for validating that the client has access and authority to call
HTTP endpoints of the service.
 
For the purpose of integration testing of each service in isolation from the other system, added capability of mocked auth. When the service is 
started with environment variable `TEST_ENVIRONMENT=true`, service starts accepting `Authorization` header in special format: `Mock <subject>:<role>`, allowing integration
tests to call service API from perspective of certain user and role, bypassing JWT token issuing. Usual auth by means of JWT tokens works as well, but requests
without any authorization is prohibited even in testing mode.
