package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.*;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Secured({"ROLE_TENANT","ROLE_TENANT_USER"})
public class SaasyApiService {
    private final AppRepo appRepo;
    private final TenantRepo tenantRepo;
    private final TenantUserRepo tenantUserRepo;
    private final UserInfoService userInfoService;

    public SaasyApiService(AppRepo appRepo, TenantRepo tenantRepo, TenantUserRepo tenantUserRepo, UserInfoService userInfoService) {
        this.appRepo = appRepo;
        this.tenantRepo = tenantRepo;
        this.tenantUserRepo = tenantUserRepo;
        this.userInfoService = userInfoService;
    }

    public Mono<AppDto> findAppById(String id) {
        return this.appRepo.findById(id).map(a-> AppDto.builder()
                .roles(a.getRoles())
                .plans(a.getPlans())
                .id(a.getId())
                .name(a.getName()).build());
    }

    public Flux<Tenant> findMyTenants() {
        User user = userInfoService.getLoggedInUser();
        List<String> tenantIds = tenantUserRepo.findAllByEmail(user.getEmail()).map(TenantUser::getTenantId).collectList().block();
        return tenantRepo.findAllById(tenantIds != null? tenantIds:new ArrayList<>());
    }

    public Mono<TenantUser> findMe(String tenantId) {
        User user = userInfoService.getLoggedInUser();
        return tenantUserRepo.findByTenantIdAndEmail(tenantId,user.getEmail());
    }
}
