package com.skapral.parrot.itests.utils.amqp;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ParrotAmqp {
    public static ConnectionFactory AMQP_CONN_FACTORY;

    static {
        AMQP_CONN_FACTORY = new ConnectionFactory();
        AMQP_CONN_FACTORY.setHost("localhost");
        AMQP_CONN_FACTORY.setUsername("guest");
        AMQP_CONN_FACTORY.setPassword("guest");
    }

    public static Connection newConnection() {
        try {
            return AMQP_CONN_FACTORY.newConnection();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
