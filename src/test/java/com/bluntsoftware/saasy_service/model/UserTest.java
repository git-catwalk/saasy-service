package com.bluntsoftware.saasy_service.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class UserTest {

  @Test
  void shouldCreateUser(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(User.class));
  }

  @Test
  void shouldBuildUser(){
    Assertions.assertNotNull(User.builder().build());
  }
}
