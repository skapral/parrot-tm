package com.skapral.parrot.common.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skapral.parrot.common.JacksonConfig;
import com.skapral.parrot.common.data.SpringDataJdbc;
import com.skapral.parrot.common.events.miner.EventsMiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;

@Configuration
@Import({JacksonConfig.class, SpringDataJdbc.class})
@EnableRabbit
@EnableScheduling
@Slf4j
public class EventsConfig {
    private static final String OUTBOX_SQL;

    static {
        try {
            OUTBOX_SQL = IOUtils.toString(EventsConfig.class.getClassLoader().getResourceAsStream("initOutbox.sql"), Charset.defaultCharset());
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private final DataSource dataSource;

    @Autowired
    public EventsConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void initOutboxTable() {
        log.info("datasource = " + dataSource);
        try(var conn = dataSource.getConnection()) {
            try(var s = conn.createStatement()) {
                s.execute(OUTBOX_SQL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to initialize event outbox", ex);
        }
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        var converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setAssumeSupportedContentType(false);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public EventsMiner eventsMiner(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        return new EventsMiner(
            jdbcTemplate,
            rabbitTemplate
        );
    }
}
