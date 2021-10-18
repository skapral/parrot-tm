package com.skapral.parrot.tasks.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("hello")
public class HelloRest {
    @GetMapping
    public String sayHello() {
        return "Hello!";
    }
}
