package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.Assertion;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.utils.amqp.AmqpEavesdropper;
import com.skapral.parrot.itests.utils.amqp.ParrotAmqp;
import org.assertj.core.api.Assertions;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TasksTest extends TestsSuite {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
            .withEnv("TEST_ENVIRONMENT", "true")
            .withServices("postgres", "amqp", "tasks-service")
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
                     new Assertion() {
                         @Override
                         public final void check() throws Exception {
                             var host = ENVIRONMENT.getServiceHost("tasks-service", 8080) + ":" + ENVIRONMENT.getServicePort("tasks-service", 8080);

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
                 )
             )
        );
    }
}
