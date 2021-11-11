package com.skapral.parrot.tasks;


import com.skapral.parrot.common.data.SpringDataJdbc;
import com.skapral.parrot.tasks.events.EventsInbox;
import com.skapral.parrot.tasks.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.Random;

@SpringBootApplication
@ComponentScan("com.skapral.parrot.tasks.rest")
@Import({EventsInbox.class, SpringDataJdbc.class, SecurityConfig.class})
public class Main {
    public static void main(String... args) {
        SpringApplication.run(Main.class);
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
