package com.skapral.parrot.itests.utils.environment;

import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
    public final Memory deploymentScopedMemory() {
        return memory;
    }

    @Override
    public final URI serviceURI(String serviceName, int servicePort) {
        var host = dockerComposeContainer.getServiceHost(serviceName, servicePort);
        var port = dockerComposeContainer.getServicePort(serviceName, servicePort);
        return URI.create("http://" + host + ":" + port);
    }

    @Override
    public final DataSource datasource(String serviceName, int servicePort, String dbName, String user, String password) {
        return memory.memoized(
            new MemoizedCallable<>() {
                @Override
                public final DataSource call() {
                    var host = dockerComposeContainer.getServiceHost(serviceName, servicePort);
                    var port = dockerComposeContainer.getServicePort(serviceName, servicePort);
                    var jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s", host, port, dbName, user, password);
                    var config = new HikariConfig();
                    config.setJdbcUrl(jdbcUrl);
                    config.setDriverClassName("org.postgresql.Driver");
                    return new HikariDataSource(config);
                }
            }
        );
    }

    @Override
    public final Connection amqp(String serviceName, int servicePort, String user, String password) {
        return memory.memoized(
            new MemoizedCallable<>() {
                @Override
                public final Connection call() {
                    try {
                        var host = dockerComposeContainer.getServiceHost(serviceName, servicePort);
                        var port = dockerComposeContainer.getServicePort(serviceName, servicePort);
                        var amqpFactory = new ConnectionFactory();
                        amqpFactory.setHost(host);
                        amqpFactory.setPort(port);
                        amqpFactory.setUsername(user);
                        amqpFactory.setPassword(password);
                        amqpFactory.setAutomaticRecoveryEnabled(false);
                        var connection = amqpFactory.newConnection();
                        return connection;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        );
    }
}
