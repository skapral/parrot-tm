package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.memoized.chm.MemoryCHM;
import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.utils.environment.Deployment;
import com.skapral.parrot.itests.utils.environment.DockerComposeDeployment;
import org.testcontainers.containers.DockerComposeContainer;

import java.util.function.Function;
import java.util.function.Supplier;

public class AssertOnTestcontainersDeployment implements Assertion {
    private final Supplier<DockerComposeContainer<?>> dockerComposeContainerSeed;
    private final Function<Deployment, Assertion> assertionOnDeployment;

    public AssertOnTestcontainersDeployment(Supplier<DockerComposeContainer<?>> dockerComposeContainerSeed, Function<Deployment, Assertion> assertionOnDeployment) {
        this.dockerComposeContainerSeed = dockerComposeContainerSeed;
        this.assertionOnDeployment = assertionOnDeployment;
    }

    @Override
    public final void check() throws Exception {
        try(var dockerComposeContainer = dockerComposeContainerSeed.get()) {
            dockerComposeContainer.start();
            assertionOnDeployment.apply(new DockerComposeDeployment(new MemoryCHM(), dockerComposeContainer)).check();
        }
    }
}
