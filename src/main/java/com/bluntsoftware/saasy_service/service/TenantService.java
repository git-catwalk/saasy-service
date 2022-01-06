package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.Roles;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.TenantUser;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
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
public class TenantService{

  private final TenantRepo repo;
  private final UserInfoService userInfoService;
  private final TenantUserRepo tenantUserRepo;

  public TenantService(TenantRepo repo, UserInfoService userService, TenantUserRepo tenantUserRepo) {
    this.repo = repo;
    this.userInfoService = userService;
    this.tenantUserRepo = tenantUserRepo;
  }

  void isOwner(User user, String id){
    Tenant current = !StringUtils.isEmpty(id) ? repo.findById(id).block() : null;
    if (current != null){
      //is tenant owner
      if(current.getCustomer().getEmail().equalsIgnoreCase(user.getEmail())){
        return;
      }else if(userInfoService.hasRole(Roles.TENANT_USER)){
        throw new RuntimeException("Unauthorized to edit this request");
      }
      if (!userInfoService.isAdmin() && !user.getUsername().equalsIgnoreCase(current.getOwner())) {
        throw new RuntimeException("Unauthorized to edit this request");
      }
    }
  }

  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
  public Mono<Tenant> save(Tenant item) {
    User user= userInfoService.getLoggedInUser();
    String id = item.getId();
    isOwner(user,id);
    if(!userInfoService.isAdmin() || id == null){
      item.setOwner(user.getUsername());
    }
    Tenant t = repo.save(item).block();
    User customer = t.getCustomer();
    TenantUser  tenantCustomer = tenantUserRepo.findByTenantIdAndEmail(t.getId(),customer.getEmail()).block();
    if(tenantCustomer == null){
      TenantUser tu = TenantUser.builder()
              .tenantId(t.getId())
              .email(customer.getEmail())
              .name(customer.getName())
              .active(customer.getActive())
              .isCustomer(true)
              .roles(customer.getRoles()).build();
      tu = tenantUserRepo.save(tu).block();
    }
    return Mono.just(t);
  }
  @Secured({"ROLE_SAASY_ADMIN","ROLE_SAASY_USER"})
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
    User user = userInfoService.getLoggedInUser();
    if(userInfoService.isAdmin()){
      return repo.findAllBy(pageable);
    }
    return repo.findAllByOwner(user.getUsername(),pageable);
  }
}
