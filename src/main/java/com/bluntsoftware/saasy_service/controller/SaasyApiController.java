package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.model.AppDto;
import com.bluntsoftware.saasy_service.model.IdName;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.TenantUser;
import com.bluntsoftware.saasy_service.service.SaasyApiService;
import com.bluntsoftware.saasy_service.service.TenantService;
import com.bluntsoftware.saasy_service.service.TenantUserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SaasyApiController {

    private final TenantService tenantService;
    private final TenantUserService tenantUserService;
    private final SaasyApiService saasyApiService;

    public SaasyApiController(TenantUserService tenantUserService,  TenantService tenantService, SaasyApiService saasyApiService) {
        this.tenantUserService = tenantUserService;
        this.tenantService = tenantService;
        this.saasyApiService = saasyApiService;
    }

    @ResponseBody
    @PostMapping(value = {"/my-tenants"}, produces = { "application/json" })
    public Flux<IdName> findMyTenants(@RequestBody Map<String,Object> info){
        return this.saasyApiService.findMyTenants()
                .filter(p->p.getApp().getId().equalsIgnoreCase(info.get("appId").toString()))
                .map(t->IdName.builder().id(t.getId()).name(t.getDisplayName()).build());
    }

    @ResponseBody
    @PostMapping(value = {"/me"}, produces = { "application/json" })
    public Mono<TenantUser> me(@RequestBody Map<String,Object> info){
        return this.saasyApiService.findMe(info.get("tenantId").toString());
    }

    @PostMapping(value="/tenant",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Tenant tenant = mapper.convertValue(dto,Tenant.class);
        return this.tenantService.save(tenant);
    }

    @GetMapping(value = "/tenant/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> findById(@PathVariable("id") String id ){
        return this.tenantService.findById(String.valueOf(id));
    }


    @ResponseBody
    @PatchMapping(value = {"/tenant-user"}, produces = { "application/json" })
    public Mono<TenantUser> update(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TenantUser tenantUser = mapper.convertValue(dto,TenantUser.class);
        return this.tenantUserService.update(tenantUser);
    }

    @ResponseBody
    @GetMapping(value = {"/tenant-user/search"}, produces = { "application/json" })
    public Flux<TenantUser> search(@RequestParam(value = "tenantId",required = false) String tenantId,
                                   @RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                   @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                   @RequestParam(value = "limit", defaultValue = "50") Integer limit){
        return this.tenantUserService.searchByTenant(tenantId, PageRequest.of(page,limit));
    }

    @ResponseBody
    @GetMapping(value = {"/app/{id}"}, produces = { "application/json" })
    public Mono<AppDto> findAppById(@PathVariable("id") String id){
        return  saasyApiService.findAppById(id);
    }

    @PostMapping(value="/app/{id}/subscribe",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> subscribe(@PathVariable("id") String id, @RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        return Mono.empty();
    }

    @PostMapping(value="/app/{id}/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> register(@PathVariable("id") String appId, @RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return Mono.empty();
    }
}
