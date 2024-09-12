package com.example.application.errors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/trigger-error")
    public String triggerError() {
        throw new RuntimeException("This is a test exception to trigger a 500 error");
    }
}
