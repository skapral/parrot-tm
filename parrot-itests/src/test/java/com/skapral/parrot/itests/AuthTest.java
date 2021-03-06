package com.skapral.parrot.itests;

import com.auth0.jwt.algorithms.Algorithm;
import com.pragmaticobjects.oo.tests.AssertCombined;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.assertions.amqp.AmqpSource;
import com.skapral.parrot.itests.assertions.amqp.AssertExpectingMessagesOnAmqp;
import com.skapral.parrot.itests.assertions.amqp.AssertMessageBody;
import com.skapral.parrot.itests.assertions.amqp.AssertMessageType;
import com.skapral.parrot.itests.assertions.amqp.expectations.AssertionBasedExpectation;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectMessage;
import com.skapral.parrot.itests.assertions.business.AssertAuthenticationFailure;
import com.skapral.parrot.itests.assertions.business.AssertCurrentUser;
import com.skapral.parrot.itests.assertions.business.UserRegistration;
import com.skapral.parrot.itests.assertions.http.AssertHttp;
import com.skapral.parrot.itests.assertions.http.StatusCode403;
import com.skapral.parrot.itests.assertions.http.endpoints.RegisterUser;
import com.skapral.parrot.itests.assertions.jdbc.AssertAssumingDbState;
import com.skapral.parrot.itests.assertions.json.AssertJsonHas;
import com.skapral.parrot.itests.assertions.jwt.AssertJwtHasClaim;
import com.skapral.parrot.itests.utils.authentication.EldrichJwtTokenAuthorization;
import com.skapral.parrot.itests.utils.authentication.JwtAuthentication;
import io.vavr.collection.List;
import org.apache.commons.io.IOUtils;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public class AuthTest extends TestsSuite {
    private static final String TEST_USERS_SQL;

    private static final Supplier<DockerComposeContainer<?>> ENVIRONMENT = () -> new DockerComposeContainer<>(new File("target/test-classes/docker-compose.yml"))
                    .withServices("postgres", "amqp", "auth-service")
                    .withExposedService("auth-service", 8080)
                    .withExposedService("amqp", 5672)
                    .withExposedService("postgres", 5432)
                    .withLogConsumer("auth-service", new Slf4jLogConsumer(LoggerFactory.getLogger("AUTH      ")))
                    .withLogConsumer("amqp", new Slf4jLogConsumer(LoggerFactory.getLogger("AMQP      ")))
                    .waitingFor("auth-service", Wait.forLogMessage(".*Started Main.*", 1))
                    .waitingFor("amqp", Wait.forLogMessage(".*Server startup complete.*", 1));

    static {
        try {
            TEST_USERS_SQL = IOUtils.resourceToString("sql/testUsers.sql", Charset.defaultCharset(), AuthTest.class.getClassLoader());
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public AuthTest() {
        super(
            new TestCase(
                "registering new user",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpectingMessagesOnAmqp(
                        new UserRegistration(
                            new JwtAuthentication(
                                deployment.deploymentScopedMemory(),
                                deployment.serviceURI("auth-service", 8080),
                                "admin"
                            ),
                            deployment.serviceURI("auth-service", 8080),
                            "testuser",
                            "PARROT"
                        ),
                        deployment.amqp("amqp", 5672, "guest", "guest"),
                        List.of(
                            new AmqpSource("outbox", "")
                        ),
                        deliveries -> new ExpectMessage(
                            deliveries,
                            msg -> new AssertionBasedExpectation(
                                new AssertCombined(
                                    new AssertMessageType(msg, "USER_NEW"),
                                    new AssertMessageBody(
                                        msg,
                                        body -> new AssertJsonHas(
                                            body,
                                            "{\"login\": \"testuser\"}",
                                            JSONCompareMode.LENIENT
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "logged in user has predicted role",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertAssumingDbState(
                        deployment.datasource("postgres", 5432, "auth", "postgres", "admin"),
                        TEST_USERS_SQL,
                        new AssertJwtHasClaim(
                            new JwtAuthentication(
                                deployment.deploymentScopedMemory(),
                                deployment.serviceURI("auth-service", 8080),
                                "innokentiy"
                            ),
                            "role",
                            "MANAGER"
                        )
                    )
                )
            ),
            new TestCase(
                "Getting currently logged in user info",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertCurrentUser(
                        new JwtAuthentication(
                            deployment.deploymentScopedMemory(),
                            deployment.serviceURI("auth-service", 8080),
                            "admin"
                        ),
                        deployment.serviceURI("auth-service", 8080),
                        "admin",
                        "ADMIN"
                    )
                )
            ),
            new TestCase(
                "token expires gracefully",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertAuthenticationFailure(
                        new EldrichJwtTokenAuthorization(
                                "testuser",
                                "PARROT",
                                Algorithm.HMAC256("parrot") // default secret
                        ),
                        deployment.serviceURI("auth-service", 8080).resolve("/current")
                    )
                )
            ),
            new TestCase(
                "User without admin rights cannot register another user",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertAssumingDbState(
                        deployment.datasource("postgres", 5432, "auth", "postgres", "admin"),
                        TEST_USERS_SQL,
                        new AssertHttp(
                            new RegisterUser(
                                new JwtAuthentication(
                                    deployment.deploymentScopedMemory(),
                                    deployment.serviceURI("auth-service", 8080),
                                    "innokentiy"
                                ),
                                deployment.serviceURI("auth-service", 8080),
                                "testuser",
                                "PARROT"
                            ),
                            resp -> new StatusCode403(resp)
                        )
                    )
                )
            )
        );
    }
}