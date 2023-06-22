package com.bluntsoftware.saasy_service.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class PlanTest {

  @Test
  void shouldCreatePlan(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Plan.class));
  }

  @Test
  void shouldBuildPlan(){
    Assertions.assertNotNull(Plan.builder().build());
  }
}
