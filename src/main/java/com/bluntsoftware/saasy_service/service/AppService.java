package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@Secured({"ROLE_SAASY_USER","ROLE_SAASY_ADMIN"})
public class AppService{

  private final AppRepo repo;
  private final UserInfoService userService;

  public AppService(AppRepo repo, UserInfoService userService) {
    this.repo = repo;
    this.userService = userService;
  }

  void isOwner(User user,String id){
    App current = !StringUtils.isEmpty(id) ? repo.findById(id).block() : null;
    if (current != null && !userService.isAdmin() && !user.getUsername().equalsIgnoreCase(current.getOwner())) {
      throw new RuntimeException("Unauthorized to edit this request");
    }
  }
  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
  public Mono<App> save(App item) {
    User user = userService.getLoggedInUser();
    String id = item.getId();
    isOwner(user,id);
    if(!userService.isAdmin() || id == null){
      item.setOwner(user.getUsername());
    }
    return repo.save(item);
  }

  public Mono<Void> deleteById(String id) {
    isOwner(userService.getLoggedInUser(),id);
    return repo.deleteById(id);
  }

  public Mono<App> findById(String id) {
    isOwner(userService.getLoggedInUser(),id);
    return repo.findById(id);
  }

  @Secured({"ROLE_SAASY_ADMIN"})
  public Flux<App> findAll() {
    return repo.findAll();
  }

  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
  public Flux<App> search(String term,Pageable pageable) {
    User user = userService.getLoggedInUser();
    log.info("create a filter for search term {}",term);
    if(userService.isAdmin()){
      return repo.findAllBy(pageable);
    }
    return repo.findAllByOwner(user.getUsername(),pageable);
  }

}
