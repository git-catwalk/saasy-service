package com.bluntsoftware.saasy_service.utils;

import com.bluntsoftware.saasy_service.model.IdName;
import com.bluntsoftware.saasy_service.model.Tenant;
import com.bluntsoftware.saasy_service.model.User;
import org.springframework.data.domain.Example;

public class QueryHelper {

    public static Example<Tenant> byEmailAndAppId(String email,String appId){
        Tenant tenantProbe = Tenant.builder()
                .app(IdName.builder().id(appId).build())
                .customer(User.builder().email(email).build()).build();
       return Example.of(tenantProbe);
    }

}
