package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class AppService{

  private final AppRepo repo;

  public AppService(AppRepo repo) {
    this.repo = repo;
  }

  public Mono<App> save(App item) {
    return repo.save(item);
  }

  public Mono<Void> deleteById(String id) {
    return repo.deleteById(id);
  }

  public Mono<App> findById(String id) {
    return repo.findById(id);
  }

  public Flux<App> findAll() {
    return repo.findAll();
  }

  public Flux<App> search(String term,Pageable pageable) {
    log.info("create a filter in repo for search term {}",term);
    return repo.findAllBy(pageable);
  }

}
