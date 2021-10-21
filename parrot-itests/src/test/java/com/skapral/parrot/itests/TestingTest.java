package com.skapral.parrot.itests;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class TestingTest {

    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
                    .withExposedService("webui", 80)
                    .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
                    .withLogConsumer("auth-service", new Slf4jLogConsumer(LoggerFactory.getLogger("AUTH      ")))
                    .withLogConsumer("analytics-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ANALYTICS ")))
                    .withLogConsumer("accounting-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ACCOUNTING")))
                    .waitingFor("tasks-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("auth-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("analytics-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("accounting-service", Wait.forLogMessage(".*Started Main.*", 1));

    @Test
    public void testNothing() {
    }
}
