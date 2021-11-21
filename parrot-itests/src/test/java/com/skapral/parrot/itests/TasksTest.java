package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.rabbitmq.client.AMQP;
import com.skapral.parrot.itests.assertions.AssertExpecting;
import com.skapral.parrot.itests.assertions.AssertExpectingNotHappening;
import com.skapral.parrot.itests.assertions.amqp.*;
import com.skapral.parrot.itests.assertions.amqp.expectations.AssertionBasedExpectation;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectAll;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectMessage;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectMessageAbsense;
import com.skapral.parrot.itests.assertions.business.TaskCreation;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode2XX;
import com.skapral.parrot.itests.assertions.http.endpoints.AssignTasks;
import com.skapral.parrot.itests.assertions.http.endpoints.GetListOfTasks;
import com.skapral.parrot.itests.assertions.jdbc.AssertAssumingDbState;
import com.skapral.parrot.itests.assertions.jdbc.AssertTableHasNumberOfRows;
import com.skapral.parrot.itests.assertions.json.AssertJsonHas;
import com.skapral.parrot.itests.utils.authentication.FakeAuthentication;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
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
        .withLogConsumer("amqp", new Slf4jLogConsumer(LoggerFactory.getLogger("AMQP      ")))
        .waitingFor("tasks-service", Wait.forLogMessage(".*Started Main.*", 1))
        .waitingFor("amqp", Wait.forLogMessage(".*Server startup complete.*", 1));

    private static final String SAMPLE_TASKS_SQL;
    private static final String SOLO_ASSIGNEE_CANDIDATE_SQL;

    static {
        try {
            SAMPLE_TASKS_SQL = IOUtils.resourceToString("sql/sampleTasks.sql", Charset.defaultCharset(), AuthTest.class.getClassLoader());
            SOLO_ASSIGNEE_CANDIDATE_SQL = IOUtils.resourceToString("sql/soloAssigneeCandidate.sql", Charset.defaultCharset(), AuthTest.class.getClassLoader());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public TasksTest() {
        super(
            new TestCase(
                "new assignee must be created when event about new user comes",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpecting(
                        new AssumingIncomingAmqpMessages(
                            deployment.amqp("amqp", 5672, "guest", "guest"),
                            "outbox",
                            "",
                            new AmqpMessage(
                                new AMQP.BasicProperties.Builder()
                                    .contentType("text/plain")
                                    .type("USER_NEW")
                                    .build(),
                                new JSONObject(
                                    HashMap.empty()
                                        .put("id", "99999999-9999-9999-9999-999999999999")
                                        .put("login", "testuser")
                                        .put("role", "PARROT")
                                        .toJavaMap()
                                ).toString()
                            )
                        ),
                        new AssertionBasedExpectation(
                            new AssertTableHasNumberOfRows(
                                deployment.datasource("postgres", 5432, "tasks", "postgres", "admin"),
                                "assignee",
                                1
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "assignees should not be created when new user is not PARROT",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpectingNotHappening(
                        new AssumingIncomingAmqpMessages(
                            deployment.amqp("amqp", 5672, "guest", "guest"),
                            "outbox",
                            "",
                            new AmqpMessage(
                                new AMQP.BasicProperties.Builder()
                                    .contentType("text/plain")
                                    .type("USER_NEW")
                                    .build(),
                                new JSONObject(
                                    HashMap.empty()
                                        .put("id", "99999999-9999-9999-9999-999999999999")
                                        .put("login", "testuser")
                                        .put("role", "MANAGER")
                                        .toJavaMap()
                                ).toString()
                            )
                        ),
                        new AssertionBasedExpectation(
                            new AssertTableHasNumberOfRows(
                                deployment.datasource("postgres", 5432, "tasks", "postgres", "admin"),
                                "assignee",
                                1
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "user creates new task, but there are no assignees for it - reassigning tasks should not happen",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpectingMessagesOnAmqp(
                        new TaskCreation(
                            deployment.serviceURI("tasks-service", 8080),
                            new FakeAuthentication("phantom", "MANAGER"),
                            "TestTask"
                        ),
                        deployment.amqp("amqp", 5672, "guest", "guest"),
                        List.of(
                            new AmqpSource("outbox", "")
                        ),
                        deliveries -> new ExpectAll(
                            new ExpectMessageAbsense(
                                deliveries,
                                delivery -> new AssertionBasedExpectation(
                                    new AssertMessageType(delivery, "TASKS_REASSIGNED")
                                )
                            ),
                            new ExpectMessage(
                                deliveries,
                                delivery -> new AssertionBasedExpectation(
                                    new AssertMessageType(delivery, "TASK_NEW")
                                )
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "user creates new task, and it is automatically assigned",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertAssumingDbState(
                        deployment.datasource("postgres", 5432, "tasks", "postgres", "admin"),
                        SOLO_ASSIGNEE_CANDIDATE_SQL,
                        new AssertExpectingMessagesOnAmqp(
                            new TaskCreation(
                                deployment.serviceURI("tasks-service", 8080),
                                new FakeAuthentication("phantom", "MANAGER"),
                                "TestTask"
                            ),
                            deployment.amqp("amqp", 5672, "guest", "guest"),
                            List.of(
                                new AmqpSource("outbox", "")
                            ),
                            deliveries -> new ExpectAll(
                                new ExpectMessage(
                                    deliveries,
                                    delivery -> new AssertionBasedExpectation(
                                        new AssertCombined(
                                            new AssertMessageType(delivery, "TASKS_REASSIGNED"),
                                            new AssertMessageBody(
                                                delivery,
                                                body -> new AssertJsonHas(
                                                    body,
                                                    "{\"list\": [{\"assigneeId\":\"99999999-9999-9999-9999-999999999999\", \"taskId\":\"_\"}]}",
                                                    JSONCompareMode.LENIENT
                                                )
                                            )
                                        )
                                    )
                                ),
                                new ExpectMessage(
                                    deliveries,
                                    delivery -> new AssertionBasedExpectation(
                                        new AssertMessageType(delivery, "TASK_NEW")
                                    )
                                )
                            )
                        )
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
                        new AssertHttp(
                            new GetListOfTasks(
                                new FakeAuthentication(
                                    "phantom",
                                    "PARROT"
                                ),
                                deployment.serviceURI("tasks-service", 8080)
                            ),
                            resp -> new AssertCombined(
                                new StatusCode2XX(resp),
                                new AssertJsonHas(resp.body(), "[_, _, _]", JSONCompareMode.STRICT)
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "assigning all tasks",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertHttp(
                        new AssignTasks(
                            new FakeAuthentication(
                                "phantom",
                                "MANAGER"
                            ),
                            deployment.serviceURI("tasks-service", 8080)
                        ),
                        resp -> new AssertCombined(
                            new StatusCode2XX(resp)
                        )
                    )
                )
            )
        );
    }
}
