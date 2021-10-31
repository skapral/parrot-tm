package com.skapral.parrot.itests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Testcontainers
public class TestingTest {
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
                    .withEnv("TEST_ENVIRONMENT", "false")
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
    public void testAuthentication() throws Exception {
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost/auth/login?login=testuser"))
                .build();
        var resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(resp.headers().map()).containsKey("Authorization");
    }
}
