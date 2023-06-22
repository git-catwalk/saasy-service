package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.dto.AppDto;
import com.bluntsoftware.saasy_service.service.PublicApiService;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public")
public class PublicApiController {
    private final PublicApiService publicApiService;

    public PublicApiController(PublicApiService publicApiService) {
        this.publicApiService = publicApiService;
    }

    @ResponseBody
    @GetMapping(value = {"/app/{id}"}, produces = { "application/json" })
    public Mono<AppDto> findAppById(@PathVariable("id") String id){
        return  publicApiService.findAppById(id);
    }
}
