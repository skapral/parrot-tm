
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

### [Authentication service](parrot-auth)

Authentication subsystem, responcible for managing users, their roles, authenticating them into the system and issuing JWT tokens.

### [Tasks service](parrot-tasks)

The service, responcible for managing the tasks, their status and assignments

### [Accounting service](parrot-accounting)

TODO: billing and accounting service

### [Analytics service](parrot-analytics)

TODO: analytics service

### [Parrot WEB UI](parrot-webui)

Frontend component of the system, contains all static HTML/CSS/JS resources, plus NGinx gateway service, that hosts them.

### [Parrot ITests](parrot-itests)

A set of integration tests for the system.

## Architecture

![PlantUML diagramm](https://www.planttext.com/api/plantuml/img/LL1B2eCm5Dpd5C5zF868oKO4BMt9ejlN6AoeIKcU5M_V14N8QWRcypwLHt2uBZCXeD0wUWVdB1AYxFArHXkasG2eDzWpMXItkhczofA2ftNYWHK_p6lt6vNEeimJ6S5FFj5b6bVAhWOJXqWT1kOTHvMsj8bdqTx3eDdRsKcxkE-J2z78xd6it8NAupxAUhiPLiOhQie2uIVA4yM9OGAdHPwNCIql9YxMNenKslJXbNy0)

## Authentication/Authorization

Authentication and authorization are implemented in [auth-common](parrot-commons/auth-common) as two Spring Security configurations.
The module is shared among the services as a usual Jar dependency.

Authentication is done by means of JWT tokens. The side that issues the tokens is `auth-service`. Tokens secret and expiration times are provided
by means of Spring Configs, with default values of "parrot" and "1 day" correspondingly. Configuration for authentication is located
here: `com.skapral.parrot.auth.common.security.AuthenticationConfig`

JWT token uses the user's UUID as a token's subject. Also, JWT token has "roles" claim, with user roles inside.

Authorization config is `com.skapral.parrot.auth.common.security.SecurityConfig`. It is imported by all the services. It is
assumed, that all services will share common secret, and therefore will be able to authorize incoming requests using the token, issued by `auth-service`.
 
For the purpose of integration testing of each service in isolation from the other system, added capability of mocking authorization. When the service is 
started with environment variable `TEST_ENVIRONMENT=true`, service accepts `Authorization` header in special format: `Mock <subject>:<role>`, allowing integration
tests to call service API from perspective of certain user and role, bypassing JWT token issuing. 
