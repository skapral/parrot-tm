package com.skapral.parrot.accounting;

import com.skapral.parrot.accounting.events.EventsConfig;
import com.skapral.parrot.accounting.security.SecurityConfig;
import com.skapral.parrot.common.data.SpringDataJdbc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfig.class, EventsConfig.class, SpringDataJdbc.class})
public class Main {
    public static void main(String... args) {
            SpringApplication.run(Main.class);
    }
}
