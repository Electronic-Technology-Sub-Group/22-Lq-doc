package com.example.shopping.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "SHOPPING-USER-SERVICE", fallback = UserServiceFallback.class)
public interface UserService extends com.example.shopping.api.UserService {
}
