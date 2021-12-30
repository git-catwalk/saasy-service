package com.bluntsoftware.saasy_service.utils;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import org.springframework.security.oauth2.jwt.*;

public class AppAwareJwtDecoder implements JwtDecoder {
private final AppRepo appRepo;
    private final NimbusJwtDecoder defaultJwtDecoder;

    public AppAwareJwtDecoder(AppRepo appRepo, String defaultJwkSetUri) {
        this.appRepo = appRepo;
        this.defaultJwtDecoder = NimbusJwtDecoder.withJwkSetUri(defaultJwkSetUri).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        String APP_ID_KEY = "SASSYAPPID";
        String[] appToken = token.split(APP_ID_KEY);
        return selectDecoder(appToken.length > 1?appToken[1]:null).decode(appToken[0]);
    }

    private JwtDecoder selectDecoder( String appId)   {
        if(appId != null){
            App app = appRepo.findById(appId).block();
            if(app != null && app.getJwkSetUri() != null && !app.getJwkSetUri().isEmpty()){
                return NimbusJwtDecoder.withJwkSetUri(app.getJwkSetUri()).build();
            }
        }
        return defaultJwtDecoder;
    }

}
