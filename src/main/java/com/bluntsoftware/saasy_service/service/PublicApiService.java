package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.dto.AppDto;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PublicApiService {
    private final AppRepo appRepo;

    public PublicApiService(AppRepo appRepo) {
        this.appRepo = appRepo;
    }

    public Mono<AppDto> findAppById(String id) {
        return this.appRepo.findById(id).map(a-> AppDto.builder()
                .roles(a.getRoles())
                .plans(a.getPlans())
                .id(a.getId())
                .name(a.getName()).build());
    }
}
