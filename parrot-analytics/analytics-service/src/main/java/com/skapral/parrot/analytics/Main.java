package com.skapral.parrot.analytics;

import com.skapral.parrot.analytics.events.EventsConfig;
import com.skapral.parrot.analytics.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfig.class, EventsConfig.class})
public class Main {
    public static void main(String... args) {
            SpringApplication.run(Main.class);
    }
}
