package com.example.ead2project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
@Tag(name = "Hello", description = "Hello API")
public class HelloController {

    @Operation(
        summary = "Say Hello",
        description = "Returns a greeting message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation"
            )
        }
    )
    
    @GetMapping
    public String sayHello() {
        return "Hello, Swagger!";
    }
}

