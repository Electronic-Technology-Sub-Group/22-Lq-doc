package com.example.helloworldserviceconsumer.api;

import com.example.helloworldserviceconsumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloConsumerEndpoint {

    @Autowired
    HelloService helloService;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return helloService.hello(name);
    }
}
