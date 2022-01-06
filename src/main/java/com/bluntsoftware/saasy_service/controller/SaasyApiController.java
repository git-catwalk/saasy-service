package com.bluntsoftware.saasy_service.controller;

import com.bluntsoftware.saasy_service.model.AppDto;
import com.bluntsoftware.saasy_service.model.IdName;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.TenantUser;
import com.bluntsoftware.saasy_service.service.SaasyApiService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SaasyApiController {

    private final SaasyApiService saasyApiService;

    public SaasyApiController(SaasyApiService saasyApiService) {
        this.saasyApiService = saasyApiService;

    }
    /********************************************************************************************
     *
     *      TENANT
     *
     ********************************************************************************************/
    @ResponseBody
    @PostMapping(value = {"/my-tenants"}, produces = { "application/json" })
    public Flux<IdName> findMyTenants(@RequestBody Map<String,Object> info){
        return this.saasyApiService.findMyTenants()
                .filter(p->p.getApp().getId().equalsIgnoreCase(info.get("appId").toString()))
                .map(t->IdName.builder().id(t.getId()).name(t.getDisplayName()).build());
    }

    @PostMapping(value="/tenant",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> save(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Tenant tenant = mapper.convertValue(dto,Tenant.class);
        return this.saasyApiService.updateTenant(tenant);
    }

    @GetMapping(value = "/tenant/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Tenant> findById(@PathVariable("id") String id ){
        return this.saasyApiService.findTenantById(String.valueOf(id));
    }
    /********************************************************************************************
     *
     *      TENANT USER
     *
     ********************************************************************************************/

    @ResponseBody
    @PostMapping(value = {"/me"}, produces = { "application/json" })
    public Mono<TenantUser> me(@RequestBody Map<String,Object> info){
        return this.saasyApiService.findMe(info.get("tenantId").toString());
    }

    @ResponseBody
    @PatchMapping(value = {"/tenant-user"}, produces = { "application/json" })
    public Mono<TenantUser> update(@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TenantUser tenantUser = mapper.convertValue(dto,TenantUser.class);
        return this.saasyApiService.updateTenantUser(tenantUser);
    }

    @ResponseBody
    @PostMapping(value = {"/tenant-user"}, produces = { "application/json" })
    public Mono<TenantUser> saveTenantUser(@RequestHeader("TENANT_KEY") String tenantId,@RequestBody Map<String,Object> dto){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TenantUser tenantUser = mapper.convertValue(dto,TenantUser.class);
        tenantUser.setTenantId(tenantId);
        return this.saasyApiService.saveTenantUser(tenantUser);
    }

    @ResponseBody
    @GetMapping(value = {"/tenant-user/search"}, produces = { "application/json" })
    public Flux<TenantUser> search(@RequestHeader(value = "TENANT_KEY", required = false) String tenantId,
                                   @RequestParam(value = "term",  defaultValue = "") String searchTerm,
                                   @RequestParam(value = "page",  defaultValue = "0") Integer page,
                                   @RequestParam(value = "limit", defaultValue = "50") Integer limit){
        return this.saasyApiService.searchTenantUserByTenant(tenantId, PageRequest.of(page,limit));
    }

    /********************************************************************************************
     *
     *      APP
     *
     ********************************************************************************************/

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
