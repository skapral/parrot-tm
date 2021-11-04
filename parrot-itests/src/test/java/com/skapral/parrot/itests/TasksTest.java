package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.assertions.business.AssertExpectingNewTaskMessage;
import com.skapral.parrot.itests.assertions.business.TaskCreation;
import com.skapral.parrot.itests.utils.authentication.FakeAuthentication;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

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
                 new AssertOnTestcontainersDeployment(
                     ENVIRONMENT,
                     deployment -> new AssertExpectingNewTaskMessage(
                         new TaskCreation(
                             deployment.serviceURI("tasks-service", 8080),
                             new FakeAuthentication("phantom", "PARROT"),
                             "TestTask"
                         ),
                         deployment.amqp("amqp", 5672, "guest", "guest")
                     )
                 )
             )
        );
    }
}
