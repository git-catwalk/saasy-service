package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.service.TenantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/rest")
public class TenantController {

  private final  TenantService service;

  public  TenantController(TenantService service) {
    this.service = service;
  }

  @PostMapping(value="/tenant",produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Tenant> save(@RequestBody Map<String,Object> dto){
    ObjectMapper mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Tenant.class));
  }

  @GetMapping(value = "/tenant/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Tenant> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/tenant",produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Tenant> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/tenant/{id}")
  public Mono<Void> deleteById(@PathVariable("id") String id ){
    return this.service.deleteById(String.valueOf(id));
  }

  @ResponseBody
  @GetMapping(value = {"/tenant/search"}, produces = { "application/json" })
  public Flux<Tenant> search(@RequestParam(value = "term",  defaultValue = "") String searchTerm,
                             @RequestParam(value = "page",  defaultValue = "0") Integer page,
                             @RequestParam(value = "limit", defaultValue = "50") Integer limit){
          return this.service.search(searchTerm,PageRequest.of(page,limit));
  }

}
