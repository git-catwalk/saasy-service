package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.repository.TenantUserRepo;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.mockito.Mockito;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
@Scope("test")
class TenantServiceTest {

  @MockBean
  private TenantRepo repo;
  @MockBean
  private TenantUserRepo tenantUserRepo;
  Tenant item1;
  Tenant item2;
  TenantService service;
  @MockBean
  private UserInfoService userService;
  @BeforeEach
  void before(){
    EasyRandom generator = new EasyRandom();
    item1 = generator.nextObject(Tenant.class);
    item2 = generator.nextObject(Tenant.class);
    service = new TenantService(this.repo,userService, tenantUserRepo);
  }

  /*@Test
  void shouldSave(){
    Mockito.when(userService.getLoggedInUser()).thenReturn(User.builder().username("test").build());
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Mockito.when(repo.save(Mockito.any(Tenant.class))).thenReturn(Mono.just(item1));
    Mono<Tenant> data = service.save(Tenant.builder().build());
    Assertions.assertEquals(data.block(),item1);
  }

  @Test
  void findById(){
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Mockito.when(repo.findById(Mockito.any(String.class))).thenReturn(Mono.just(item1));
    Assertions.assertNotNull(service.findById(String.valueOf("1")).block());
  }

  @Test
  void deleteById(){
    Mockito.when(repo.findById(any(String.class))).thenReturn(Mono.just(item1));
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Mockito.when(repo.deleteById(Mockito.any(String.class))).thenReturn(Mono.empty());
    Assertions.assertNull(service.deleteById(String.valueOf("1")).block());
  }*/

  @Test
  void findAll(){
    Mockito.when(repo.findAll()).thenReturn(Flux.just(item1,item2));
    List<Tenant> all = service.findAll().collectList().block();
    Assertions.assertEquals(2, all.size());
  }
}
