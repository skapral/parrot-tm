package com.skapral.parrot.itests;

import com.skapral.parrot.itests.utils.amqp.AmqpEavesdropper;
import com.skapral.parrot.itests.utils.amqp.ParrotAmqp;
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
import java.time.Duration;

@Testcontainers
public class TasksTest {
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer environment =
    new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
            .withEnv("TEST_ENVIRONMENT", "true")
            .withServices("postgres", "amqp", "tasks-service")
            .withExposedService("tasks-service", 8080)
            .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
            .withLogConsumer("amqp", new Slf4jLogConsumer(LoggerFactory.getLogger("AMQP      ")))
            .waitingFor("tasks-service",Wait.forLogMessage(".*Started Main.*", 1))
            .waitingFor("amqp", Wait.forLogMessage(".*Server startup complete.*", 1));

    @Test
    public void taskCreation() throws Exception {
        var host = environment.getServiceHost("tasks-service", 8080) + ":" + environment.getServicePort("tasks-service", 8080);

        try(var amqpConn = ParrotAmqp.newConnection()) {
            var eavesdropper = new AmqpEavesdropper(amqpConn);
            try (var amqp = eavesdropper.startEavesdropping("outbox", "")) {
                var client = HttpClient.newHttpClient();
                var req = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .header("Authorization", "Mock testuser:PARROT")
                        .uri(URI.create("http://" + host + "/?description=TestTask"))
                        .build();
                var resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                Assertions.assertThat(resp.statusCode()).isEqualTo(200);

                amqp.assertOnMessages(messages -> {
                    var newTaskMessages = messages.filter(m -> m.getProperties().getType().equals("TASK_NEW"));
                    Assertions.assertThat(newTaskMessages).hasSize(1);
                }, Duration.ofMinutes(1));
            }
        }
    }
}
