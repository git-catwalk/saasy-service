package com.bluntsoftware.saasy_service.utils;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Map;

public class AppAwareJwtDecoder implements JwtDecoder {
    private final AppRepo appRepo;
    private final NimbusJwtDecoder defaultJwtDecoder;

    public AppAwareJwtDecoder(AppRepo appRepo, String defaultJwkSetUri) {
        this.appRepo = appRepo;
        this.defaultJwtDecoder = NimbusJwtDecoder.withJwkSetUri(defaultJwkSetUri).build();
    }

    static class SaasyJwt extends Jwt{
        private final String appId;
        public String getAppId(){
            return this.appId;
        }
        public SaasyJwt(Jwt jwt,String appId) {
            super(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), jwt.getClaims());
            this.appId = appId;
        }
    }


    @SneakyThrows
    @Override
    public Jwt decode(String token) throws JwtException {
        String APP_ID_KEY = "SASSYAPPID";
        String[] appToken = token.split(APP_ID_KEY);
        String appId = appToken.length > 1?appToken[1]:null;
        if(appId == null){
            JWT jwt = JWTParser.parse(token);
            Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();
            if(claims.containsKey("appId")){
                appId = claims.get("appId").toString();
            }
        }
        Jwt jwt = selectDecoder(appId).decode(appToken[0]);
        return new SaasyJwt(jwt, appId);
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
