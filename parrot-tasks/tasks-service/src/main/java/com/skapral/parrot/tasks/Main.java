package com.skapral.parrot.tasks;


import com.skapral.parrot.tasks.data.SpringDataJdbc;
import com.skapral.parrot.tasks.events.Amqp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({Amqp.class, SpringDataJdbc.class})
public class Main {
    public static void main(String... args) {
        SpringApplication.run(Main.class);
    }
}
