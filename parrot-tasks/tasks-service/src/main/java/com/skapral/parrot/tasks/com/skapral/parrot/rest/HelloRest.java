package com.skapral.parrot.tasks.com.skapral.parrot.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class HelloRest {
    @GetMapping("hello")
    public String sayHello() {
        return "Hello!";
    }
}
