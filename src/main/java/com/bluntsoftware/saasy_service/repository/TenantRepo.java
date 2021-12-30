package com.bluntsoftware.saasy_service.repository;

import com.bluntsoftware.saasy_service.model.App;
import reactor.core.publisher.Flux;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bluntsoftware.saasy_service.model.Tenant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepo extends ReactiveMongoRepository<Tenant, String> {
	Flux<Tenant> findAllBy(Pageable pageable);
	Flux<Tenant> findAllByOwner(String owner, Pageable pageable);
	Flux<Tenant> findAllByIdIn(List<String> ids);
}
