#!/bin/sh

docker-compose -f parrot-itests/target/test-classes/docker-compose.yml $*

