package com.skapral.parrot.itests.environment;

import com.skapral.parrot.itests.utils.amqp.AmqpEavesdropper;

import javax.sql.DataSource;
import java.net.URI;

public interface Deployment {
    URI serviceURI(String serviceName, int servicePort);
    DataSource datasource(String serviceName, int servicePort, String dbName, String user, String password);
    AmqpEavesdropper amqp(String serviceName, int servicePort, String user, String password);
}
