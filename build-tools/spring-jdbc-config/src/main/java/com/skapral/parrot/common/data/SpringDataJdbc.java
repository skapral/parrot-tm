package com.skapral.parrot.common.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Function;

@Configuration
@EnableJdbcRepositories
public class SpringDataJdbc extends AbstractJdbcConfiguration {
    private static final Function<String, String> DB_QUERY = s -> String.format("SELECT datname FROM pg_catalog.pg_database WHERE datname = '%s'", s);
    private static final Logger log = LoggerFactory.getLogger(SpringDataJdbc.class);

    private final String host;
    private final String port;
    private final String database;
    private final String user;
    private final String password;

    @Autowired
    public SpringDataJdbc(
            @Value("${db.host}") String host,
            @Value("${db.port}") String port,
            @Value("${db.name}") String database,
            @Value("${db.user}") String user,
            @Value("${db.password}") String password
    ) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Bean
    public DataSource dataSource() {
        try (Connection connection = DriverManager.getConnection(
                String.format("jdbc:postgresql://%s:%s/", host, port),
                user, password)
        ) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(DB_QUERY.apply(database));
            if (!rs.next()) {
                statement.executeUpdate(String.format("CREATE DATABASE %s", database));
                log.info("Database '{}' created successfully", database);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Checking of database existence failed", e);
        }

        var jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s", host, port, database, user, password);
        var config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName("org.postgresql.Driver");
        var ds = new HikariDataSource(config);
        var flyway = Flyway.configure()
                .dataSource(ds)
                .load();
        flyway.migrate();
        return ds;
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
