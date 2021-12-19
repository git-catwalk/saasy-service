package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class TenantService{

  private final TenantRepo repo;

  public TenantService(TenantRepo repo) {
    this.repo = repo;
  }

  public Mono<Tenant> save(Tenant item) {
    return repo.save(item);
  }

  public Mono<Void> deleteById(String id) {
    return repo.deleteById(id);
  }

  public Mono<Tenant> findById(String id) {
    return repo.findById(id);
  }

  public Flux<Tenant> findAll() {
    return repo.findAll();
  }

  public Flux<Tenant> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAllBy(pageable);
  }

}
