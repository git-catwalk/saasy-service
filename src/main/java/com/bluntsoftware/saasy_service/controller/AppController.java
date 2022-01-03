package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.service.AppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class AppController {

  private final  AppService service;

  public  AppController(AppService service) {
    this.service = service;
  }

  @PostMapping(value="/app",produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<App> save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,App.class));
  }

  @GetMapping(value = "/app/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<App> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/app",produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<App> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/app/{id}")
  public Mono<Void> deleteById(@PathVariable("id") String id ){
    return this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/app/search"}, produces = { "application/json" })
  public Flux<App> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
          return this.service.search(searchTerm,PageRequest.of(page,limit));
  }
}
