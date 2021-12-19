package com.bluntsoftware.saasy_service.repository;

import reactor.core.publisher.Flux;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bluntsoftware.saasy_service.model.Tenant;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepo extends ReactiveMongoRepository<Tenant, String> {

	Flux<Tenant> findAllBy(Pageable pageable);
}