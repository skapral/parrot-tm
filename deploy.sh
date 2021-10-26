#!/bin/sh

cd parrot-itests/target/test-classes && docker-compose $* ; cd -

