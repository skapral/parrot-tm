package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.assertions.business.AssertExpectingNewTaskMessage;
import com.skapral.parrot.itests.assertions.business.AssertTasksListSize;
import com.skapral.parrot.itests.assertions.business.TaskCreation;
import com.skapral.parrot.itests.assertions.jdbc.AssertAssumingDbState;
import com.skapral.parrot.itests.utils.authentication.FakeAuthentication;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class TasksTest extends TestsSuite {

    public static Supplier<DockerComposeContainer<?>> ENVIRONMENT = () -> new DockerComposeContainer<>(new File("target/test-classes/docker-compose.yml"))
            .withEnv("TEST_ENVIRONMENT", "true")
            .withServices("postgres", "amqp", "tasks-service")
            .withExposedService("amqp", 5672)
            .withExposedService("tasks-service", 8080)
            .withExposedService("postgres", 5432)
            .withLogConsumer("tasks-service", new Slf4jLogConsumer(LoggerFactory.getLogger("TASKS     ")))
            .waitingFor("tasks-service",Wait.forLogMessage(".*Started Main.*", 1))
            .waitingFor("amqp", Wait.forLogMessage(".*Server startup complete.*", 1));

    private static final String SAMPLE_TASKS_SQL;

    static {
        try {
            SAMPLE_TASKS_SQL = IOUtils.resourceToString("sql/sampleTasks.sql", Charset.defaultCharset(), AuthTest.class.getClassLoader());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }


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
             ),
             new TestCase(
                 "user queries for tasks",
                 new AssertOnTestcontainersDeployment(
                     ENVIRONMENT,
                     deployment -> new AssertAssumingDbState(
                         deployment.datasource("postgres", 5432, "tasks", "postgres", "admin"),
                         SAMPLE_TASKS_SQL,
                         new AssertTasksListSize(
                             new FakeAuthentication(
                                 "phantom",
                                 "PARROT"
                             ),
                             deployment.serviceURI("tasks-service", 8080),
                             3
                         )
                     )
                 )
             )
        );
    }
}
