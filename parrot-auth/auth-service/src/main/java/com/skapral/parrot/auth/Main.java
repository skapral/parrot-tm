package com.skapral.parrot.auth;

import com.skapral.parrot.auth.security.AuthenticationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AuthenticationConfig.class})
public class Main {
    public static void main(String... args) {
        SpringApplication.run(Main.class);
    }
}