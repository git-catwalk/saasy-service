package com.bluntsoftware.saasy_service.repository;

import reactor.core.publisher.Flux;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bluntsoftware.saasy_service.model.App;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepo extends ReactiveMongoRepository<App, String> {

	Flux<App> findAllBy(Pageable pageable);
}