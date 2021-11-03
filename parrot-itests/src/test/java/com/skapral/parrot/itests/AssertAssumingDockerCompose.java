package com.skapral.parrot.itests;

import com.pragmaticobjects.oo.tests.Assertion;
import org.testcontainers.containers.DockerComposeContainer;

public class AssertAssumingDockerCompose implements Assertion {
    private final DockerComposeContainer<?> dockerComposeContainer;
    private final Assertion delegate;

    public AssertAssumingDockerCompose(DockerComposeContainer<?> dockerComposeContainer, Assertion delegate) {
        this.dockerComposeContainer = dockerComposeContainer;
        this.delegate = delegate;
    }

    @Override
    public final void check() throws Exception {
        dockerComposeContainer.start();
        try {
            delegate.check();
        } finally {
            dockerComposeContainer.stop();
        }
    }
}
