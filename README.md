
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

### [Parrot auth](parrot-auth)

Authentication subsystem, responcible for managing users, their roles, authenticating them into the system and issuing JWT tokens.

### [Parrot tasks](parrot-tasks)

The service, responcible for managing the tasks, their status and assignments

### [Parrot accounting](parrot-accounting)

TODO: billing and accounting service

### [Parrot analytics](parrot-analytics)

TODO: analytics service

### [Parrot WEB UI](parrot-webui)

Frontend component of the system, contains all static HTML/CSS/JS resources, plus NGinx gateway service, that hosts them.

### [Parrot ITests](parrot-itests)

A set of integration tests for the system.

