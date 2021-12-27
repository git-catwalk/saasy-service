package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class TenantService{

  private final TenantRepo repo;
  private final UserInfoService userInfoService;

  public TenantService(TenantRepo repo, UserInfoService userService) {
    this.repo = repo;
    this.userInfoService = userService;
  }
  void isOwner(User user, String id){
    Tenant current = !StringUtils.isEmpty(id) ? repo.findById(id).block() : null;
    if (current != null && !userInfoService.isAdmin() && !user.getUsername().equalsIgnoreCase(current.getOwner())) {
      throw new RuntimeException("Unauthorized to edit this request");
    }
  }
  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
  public Mono<Tenant> save(Tenant item) {
    var user= userInfoService.getLoggedInUser();
    String id = item.getId();
    isOwner(user,id);
    if(!userInfoService.isAdmin() || id == null){
      item.setOwner(user.getUsername());
    }
    return repo.save(item);
  }

  public Mono<Void> deleteById(String id) {
     isOwner(userInfoService.getLoggedInUser(),id);
    return repo.deleteById(id);
  }

  public Mono<Tenant> findById(String id) {
     isOwner(userInfoService.getLoggedInUser(),id);
    return repo.findById(id);
  }

  @Secured({"ROLE_SAASY_ADMIN"})
  public Flux<Tenant> findAll() {
    return repo.findAll();
  }

  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
  public Flux<Tenant> search(String term,Pageable pageable) {
    var user = userInfoService.getLoggedInUser();
    log.info("create a filter in repo for search term {}",term);
    log.info("create a filter for search term {}",term);
    if(userInfoService.isAdmin()){
      return repo.findAllBy(pageable);
    }
    return repo.findAllByOwner(user.getUsername(),pageable);
  }

}
