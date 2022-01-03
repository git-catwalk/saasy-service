package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.exception.BadRequestException;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.TenantUser;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TenantUserService {
    private final TenantUserRepo repo;
    private final UserInfoService userInfoService;
    private final TenantRepo tenantRepo;

    public TenantUserService(TenantUserRepo repo, UserInfoService userInfoService, TenantRepo tenantRepo) {
        this.repo = repo;
        this.userInfoService = userInfoService;
        this.tenantRepo = tenantRepo;
    }
    public Mono<TenantUser> update(TenantUser tenantUser) {
        Mono<TenantUser> mono = repo.findById(tenantUser.getId());
         TenantUser  current = mono.block();
         if(current != null){
             current.setAvatar(tenantUser.getAvatar());
             current.setName(tenantUser.getName());
             current.getOtherInfo().putAll(tenantUser.getOtherInfo());
             return save(current);
         }
        return mono;
    }

    public Mono<TenantUser> save(TenantUser tenantUser) {

        String tenantId = tenantUser.getTenantId();
        String userEmail = tenantUser.getEmail();

        if(tenantId == null || tenantId.isEmpty()) {
            throw new BadRequestException( "Tenant ID is required");
        }

        if(userEmail == null || userEmail.isEmpty()) {
            throw new BadRequestException( "User Email is required");
        }

        TenantUser current = repo.findByTenantIdAndEmail(tenantId,userEmail).block();
        if(current != null && !current.getId().equalsIgnoreCase(tenantUser.getId())){
            throw new BadRequestException("Tenant User already exists");
        }

        return repo.save(tenantUser);
    }

    public Mono<Void> deleteById(String id) {

        return repo.deleteById(id);
    }

    public Mono<TenantUser> findById(String id) {
        return repo.findById(id);
    }

    @Secured({"ROLE_SAASY_ADMIN"})
    public Flux<TenantUser> findAll() {
        return repo.findAll();
    }

    @Secured({"ROLE_SAASY_ADMIN"})
    public Flux<TenantUser> search(String term, Pageable pageable) {
        log.info("create a filter in repo for search term {}",term);
        return repo.findAllBy(pageable);
    }

    public Flux<TenantUser> searchByTenant(String tenantId, Pageable pageable) {
        return repo.findAllByTenantId(tenantId,pageable);
    }

    public Flux<Tenant> findMyTenants() {
        User user = userInfoService.getLoggedInUser();
        List<String> tenantIds = repo.findAllByEmail(user.getEmail()).map(TenantUser::getTenantId).collectList().block();
        return tenantRepo.findAllById(tenantIds != null? tenantIds:new ArrayList<>());
    }

    public Mono<TenantUser> findMe(String tenantId) {
        User user = userInfoService.getLoggedInUser();
        return repo.findByTenantIdAndEmail(tenantId,user.getEmail());
    }

}
