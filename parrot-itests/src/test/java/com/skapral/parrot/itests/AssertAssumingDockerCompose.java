package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.memoized.chm.MemoryCHM;
import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.environment.Deployment;
import com.skapral.parrot.itests.environment.DockerComposeDeployment;
import org.testcontainers.containers.DockerComposeContainer;

import java.util.function.Function;

public class AssertAssumingDockerCompose implements Assertion {
    private final DockerComposeContainer<?> dockerComposeContainer;
    private final Function<Deployment, Assertion> assertionOnDeployment;

    public AssertAssumingDockerCompose(DockerComposeContainer<?> dockerComposeContainer, Function<Deployment, Assertion> assertionOnDeployment) {
        this.dockerComposeContainer = dockerComposeContainer;
        this.assertionOnDeployment = assertionOnDeployment;
    }

    @Override
    public final void check() throws Exception {
        dockerComposeContainer.start();
        try {
            assertionOnDeployment.apply(new DockerComposeDeployment(new MemoryCHM(), dockerComposeContainer)).check();
        } finally {
            dockerComposeContainer.stop();
        }
    }
}
