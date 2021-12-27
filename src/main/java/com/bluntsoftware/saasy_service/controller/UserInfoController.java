package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/rest")
public class UserInfoController {

    private final UserInfoService userService;

    public UserInfoController(UserInfoService userService) {
        this.userService = userService;
    }

    @Operation(summary = "get the users info based on jwt bearer token", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user")
    public User getUserInfo(@AuthenticationPrincipal Jwt principal) {
        return this.userService.getUser(principal);
    }


    @PostMapping(value="/user",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> save(@RequestBody Map<String,Object> dto){
        return Mono.just(Tenant.builder().build() );
    }
}
