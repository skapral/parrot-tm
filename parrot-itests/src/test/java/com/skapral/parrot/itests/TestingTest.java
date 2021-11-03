package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.Assertion;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.rabbitmq.client.Delivery;
import com.skapral.parrot.itests.utils.amqp.AmqpEavesdropper;
import com.skapral.parrot.itests.utils.amqp.ParrotAmqp;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
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

public class TestingTest extends TestsSuite {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
                    .withExposedService("webui", 80)
                    .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
                    .withLogConsumer("auth-service", new Slf4jLogConsumer(LoggerFactory.getLogger("AUTH      ")))
                    .withLogConsumer("analytics-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ANALYTICS ")))
                    .withLogConsumer("accounting-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ACCOUNTING")))
                    .waitingFor("tasks-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("auth-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("analytics-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("accounting-service", Wait.forLogMessage(".*Started Main.*", 1));

    public TestingTest() {
        super(
                new TestCase(
                        "new user log in",
                        new AssertAssumingDockerCompose(
                            ENVIRONMENT,
                            new Assertion() {
                                @Override
                                public final void check() throws Exception {
                                    try(var amqpConn = ParrotAmqp.newConnection()) {
                                        var eavesdropper = new AmqpEavesdropper(amqpConn);
                                        try(var amqp = eavesdropper.startEavesdropping("outbox", "")) {
                                            var client = HttpClient.newHttpClient();
                                            var req = HttpRequest.newBuilder()
                                                    .POST(HttpRequest.BodyPublishers.noBody())
                                                    .uri(URI.create("http://localhost/auth/login?login=testuser"))
                                                    .build();
                                            var resp = client.send(req, HttpResponse.BodyHandlers.ofString());

                                            Assertions.assertThat(resp.headers().map()).containsKey("Authorization");

                                            amqp.assertOnMessages(messages -> {
                                                var newUserMessages = messages
                                                        .filter(m -> m.getProperties().getType().equals("USER_NEW"))
                                                        .map(Delivery::getBody)
                                                        .map(String::new)
                                                        .map(JSONObject::new);
                                                Assertions.assertThat(newUserMessages).hasSize(1);
                                                Assertions.assertThat(newUserMessages.get().getString("login")).isEqualTo("testuser");
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
