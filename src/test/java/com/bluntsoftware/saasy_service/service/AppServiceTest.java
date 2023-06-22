package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.bluntsoftware.saasy_service.model.App;
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
class AppServiceTest {

  @MockBean
  private AppRepo repo;

  App item1;
  App item2;
  AppService service;
  @MockBean
  private UserInfoService userService;
  @BeforeEach
  void before(){
    EasyRandom generator = new EasyRandom();
    item1 = generator.nextObject(App.class);
    item2 = generator.nextObject(App.class);
    service = new AppService(this.repo, userService);
  }

  @Test
  void shouldSave(){
    Mockito.when(userService.getLoggedInUser()).thenReturn(User.builder().username("test").build());
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Mockito.when(repo.save(any(App.class))).thenReturn(Mono.just(item1));
    Mono<App> data = service.save(App.builder().build());
    Assertions.assertEquals(data.block(),item1);
  }

  @Test
  void findById(){
    Mockito.when(repo.findById(any(String.class))).thenReturn(Mono.just(item1));
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Assertions.assertNotNull(service.findById(String.valueOf("1")).block());
  }

  @Test
  void deleteById(){
    Mockito.when(repo.findById(any(String.class))).thenReturn(Mono.just(item1));
    Mockito.when(userService.isAdmin()).thenReturn(true);
    Mockito.when(repo.deleteById(any(String.class))).thenReturn(Mono.empty());
    Assertions.assertNull(service.deleteById(String.valueOf("1")).block());
  }

  @Test
  void findAll(){
    Mockito.when(repo.findAll()).thenReturn(Flux.just(item1,item2));
    List<App> all = service.findAll().collectList().block();
    Assertions.assertEquals(2, all.size());
  }
}
