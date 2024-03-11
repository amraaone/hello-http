package com.example.hellohttp.hellohttp.controller;

import com.example.hellohttp.hellohttp.services.HelloServiceClient;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private final HelloServiceClient helloServiceClient;

  public GreetingController(HelloServiceClient helloServiceClient) {
    this.helloServiceClient = helloServiceClient;
  }

  @PostMapping("/greet")
  public ResponseEntity<String> greet(@RequestBody Map<String, Object> request)
    throws Exception {
    String name = request.getOrDefault("name", "defaultName").toString();

    String response = helloServiceClient.sayHello(name);

    return ResponseEntity
      .ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(response);
  }
}
