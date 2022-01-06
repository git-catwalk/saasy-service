package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.exception.BadRequestException;
import com.bluntsoftware.saasy_service.model.*;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return tenantUserRepo.findByTenantIdAndEmail(tenantId,user.getEmail())
                .switchIfEmpty( Mono.just(TenantUser.builder().name(user.getName()).email(user.getEmail()).build()));
    }

    public Mono<Tenant> updateTenant(Tenant tenant) {
        Tenant current = tenantRepo.findById(tenant.getId()).block();
        if(current != null){
            TenantUser me = findMe(tenant.getId()).block();
            if(me != null && tenant.getId().equalsIgnoreCase(me.getTenantId()) && me.getRoles().contains("ADMIN")){
                current.setCustomer(tenant.getCustomer());
                current.setDisplayName(tenant.getDisplayName());
                return tenantRepo.save(tenant);
            }
        }
        return Mono.just(current);
    }

    public Mono<Tenant> findTenantById(String id) {

        TenantUser me = findMe(id).block();
        if(me == null || !id.equalsIgnoreCase(me.getTenantId())) {
            throw new BadRequestException( "Not authorised to edit this user");
        }

        return tenantRepo.findById(id);
    }

    public Mono<TenantUser> updateTenantUser(TenantUser tenantUser) {
        Mono<TenantUser> mono = tenantUserRepo.findById(tenantUser.getId());
        TenantUser  current = mono.block();
        if(current != null){
            current.setAvatar(tenantUser.getAvatar());
            current.setName(tenantUser.getName());
            if(tenantUser.getOtherInfo() != null){
                Map<String,Object> otherInfo = current.getOtherInfo() != null ? current.getOtherInfo() : new HashMap<>();
                otherInfo.putAll(tenantUser.getOtherInfo());
                current.setOtherInfo(otherInfo);
            }
            return saveTenantUser(current);
        }
        return mono;
    }

    public Mono<TenantUser> saveTenantUser(TenantUser tenantUser) {
        String tenantId = tenantUser.getTenantId();
        String userEmail = tenantUser.getEmail();

        if(tenantId == null || tenantId.isEmpty()) {
            throw new BadRequestException( "Tenant ID is required");
        }

        if(userEmail == null || userEmail.isEmpty()) {
            throw new BadRequestException( "User Email is required");
        }

        TenantUser current = tenantUserRepo.findByTenantIdAndEmail(tenantId,userEmail).block();
        if(current != null && !current.getId().equalsIgnoreCase(tenantUser.getId())){
            throw new BadRequestException("Tenant User already exists");
        }

        TenantUser me = findMe(tenantId).block();
        if(me == null || !tenantId.equalsIgnoreCase(me.getTenantId()) || !me.getRoles().contains("ADMIN")) {
            throw new BadRequestException( "Not authorised to edit this user");
        }

        return tenantUserRepo.save(tenantUser);
    }

    public Flux<TenantUser> searchTenantUserByTenant(String tenantId, PageRequest pageable) {
        TenantUser me = findMe(tenantId).block();
        if(me == null || !tenantId.equalsIgnoreCase(me.getTenantId())) {
            throw new BadRequestException( "Not authorised to edit this user");
        }
        return tenantUserRepo.findAllByTenantId(tenantId,pageable);
    }
}
