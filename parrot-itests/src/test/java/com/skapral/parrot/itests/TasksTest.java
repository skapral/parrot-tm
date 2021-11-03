package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.AssertPass;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import org.assertj.core.api.Assertions;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TasksTest extends TestsSuite {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
            .withEnv("TEST_ENVIRONMENT", "true")
            .withServices("postgres", "amqp", "tasks-service")
            .withExposedService("amqp", 5672)
            .withExposedService("tasks-service", 8080)
            .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
            .withLogConsumer("amqp", new Slf4jLogConsumer(LoggerFactory.getLogger("AMQP      ")))
            .waitingFor("tasks-service",Wait.forLogMessage(".*Started Main.*", 1))
            .waitingFor("amqp", Wait.forLogMessage(".*Server startup complete.*", 1));


    public TasksTest() {
        super(
             new TestCase(
                 "user creates new task",
                 new AssertAssumingDockerCompose(
                     ENVIRONMENT,
                     (deployment) -> {
                         var tasksServiceUri = deployment.serviceURI("tasks-service", 8080);
                         var amqp = deployment.amqp("amqp", 5672, "guest", "guest");
                         try (var amqpState = amqp.startEavesdropping("outbox", "")) {
                             var client = HttpClient.newHttpClient();
                             var req = HttpRequest.newBuilder()
                                     .POST(HttpRequest.BodyPublishers.noBody())
                                     .header("Authorization", "Mock testuser:PARROT")
                                     .uri(tasksServiceUri.resolve("/?description=TestTask"))
                                     .build();
                             var resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                             Assertions.assertThat(resp.statusCode()).isEqualTo(200);

                             amqpState.assertOnMessages(messages -> {
                                 var newTaskMessages = messages.filter(m -> m.getProperties().getType().equals("TASK_NEW"));
                                 Assertions.assertThat(newTaskMessages).hasSize(1);
                             }, Duration.ofMinutes(1));
                         } catch(Exception ex) {
                             throw new RuntimeException(ex);
                         }
                         return new AssertPass();
                     }
                 )
             )
        );
    }
}
