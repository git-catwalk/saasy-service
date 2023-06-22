package com.bluntsoftware.saasy_service.repository;

import com.bluntsoftware.saasy_service.model.TenantUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TenantUserRepo extends ReactiveMongoRepository<TenantUser, String> {
    Mono<TenantUser> findByTenantIdAndEmail(String tenantId,String email);
    Flux<TenantUser> findAllByEmail(String email);
    Flux<TenantUser> findAllByTenantId(String tenantId,Pageable pageable);
    Flux<TenantUser> findAllBy(Pageable pageable);
}
