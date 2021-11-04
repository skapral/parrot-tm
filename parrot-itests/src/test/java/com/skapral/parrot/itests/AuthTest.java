package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.assertions.business.AssertExpectingNewUserMessage;
import com.skapral.parrot.itests.assertions.business.UserAuthentication;
import com.skapral.parrot.itests.utils.authentication.JwtAuthentication;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

public class AuthTest extends TestsSuite {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DockerComposeContainer ENVIRONMENT = new DockerComposeContainer(new File("target/test-classes/docker-compose.yml"))
                    .withExposedService("webui", 80)
                    .withExposedService("amqp", 5672)
                    .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
                    .withLogConsumer("auth-service", new Slf4jLogConsumer(LoggerFactory.getLogger("AUTH      ")))
                    .withLogConsumer("analytics-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ANALYTICS ")))
                    .withLogConsumer("accounting-service", new Slf4jLogConsumer(LoggerFactory.getLogger("ACCOUNTING")))
                    .waitingFor("tasks-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("auth-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("analytics-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("accounting-service", Wait.forLogMessage(".*Started Main.*", 1));

    public AuthTest() {
        super(
            new TestCase(
                "new user logs in",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpectingNewUserMessage(
                        new UserAuthentication(
                            new JwtAuthentication(
                                deployment.deploymentScopedMemory(),
                                deployment.serviceURI("webui", 80),
                                "testuser"
                            )
                        ),
                        deployment.amqp("amqp", 5672, "guest", "guest"),
                        "testuser"
                    )
                )
            )
        );
    }
}