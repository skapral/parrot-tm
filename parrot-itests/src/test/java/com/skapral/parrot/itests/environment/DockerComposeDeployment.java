package com.skapral.parrot.itests.environment;

import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;
import com.rabbitmq.client.ConnectionFactory;
import com.skapral.parrot.itests.utils.amqp.AmqpEavesdropper;
import org.testcontainers.containers.DockerComposeContainer;

import javax.sql.DataSource;
import java.net.URI;

public class DockerComposeDeployment implements Deployment {
    private final Memory memory;
    private final DockerComposeContainer<?> dockerComposeContainer;

    public DockerComposeDeployment(Memory memory, DockerComposeContainer<?> dockerComposeContainer) {
        this.memory = memory;
        this.dockerComposeContainer = dockerComposeContainer;
    }

    @Override
    public final URI serviceURI(String serviceName, int servicePort) {
        var host = dockerComposeContainer.getServiceHost(serviceName, servicePort);
        var port = dockerComposeContainer.getServicePort(serviceName, servicePort);
        return URI.create("http://" + host + ":" + port);
    }

    @Override
    public final DataSource datasource(String serviceName, int servicePort, String dbName, String user, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final AmqpEavesdropper amqp(String serviceName, int servicePort, String user, String password) {
        return memory.memoized(
            new MemoizedCallable<>() {
                @Override
                public final AmqpEavesdropper call() {
                    try {
                        var host = dockerComposeContainer.getServiceHost(serviceName, servicePort);
                        var port = dockerComposeContainer.getServicePort(serviceName, servicePort);
                        var amqpFactory = new ConnectionFactory();
                        amqpFactory.setHost(host);
                        amqpFactory.setPort(port);
                        amqpFactory.setUsername(user);
                        amqpFactory.setPassword(password);
                        var connection = amqpFactory.newConnection();
                        return new AmqpEavesdropper(connection);
                    } catch(Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        );
    }
}
