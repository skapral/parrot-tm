package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.skapral.parrot.itests.assertions.business.AssertCurrentUser;
import com.skapral.parrot.itests.assertions.business.AssertExpectingNewUserMessage;
import com.skapral.parrot.itests.assertions.business.UserAuthentication;
import com.skapral.parrot.itests.assertions.jdbc.AssertAssumingDbState;
import com.skapral.parrot.itests.assertions.jwt.AssertJwtHasClaim;
import com.skapral.parrot.itests.utils.authentication.JwtAuthentication;
import org.apache.commons.io.IOUtils;
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
                "new user automatically registers itself",
                new AssertOnTestcontainersDeployment(
                    ENVIRONMENT,
                    deployment -> new AssertExpectingNewUserMessage(
                        new UserAuthentication(
                            new JwtAuthentication(
                                deployment.deploymentScopedMemory(),
                                deployment.serviceURI("auth-service", 8080),
                                "testuser"
                            )
                        ),
                        deployment.amqp("amqp", 5672, "guest", "guest"),
                        "testuser"
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
                            "testuser"
                        ),
                        deployment.serviceURI("auth-service", 8080),
                        "testuser",
                        "PARROT"
                    )
                )
            )
        );
    }
}