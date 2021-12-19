package com.bluntsoftware.saasy_service.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class AppTest {

  @Test
  void shouldCreateApp(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(App.class));
  }

  @Test
  void shouldBuildApp(){
    Assertions.assertNotNull(App.builder().build());
  }
}
