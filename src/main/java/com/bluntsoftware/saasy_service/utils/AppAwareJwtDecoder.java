package com.bluntsoftware.saasy_service.utils;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

public class AppAwareJwtDecoder implements JwtDecoder {
private final AppRepo appRepo;
    private final NimbusJwtDecoder defaultJwtDecoder;

    public AppAwareJwtDecoder(AppRepo appRepo, String defaultJwkSetUri) {
        this.appRepo = appRepo;
        this.defaultJwtDecoder = NimbusJwtDecoder.withJwkSetUri(defaultJwkSetUri).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return selectDecoder(token).decode(token);
    }

    private JwtDecoder selectDecoder(String token)   {
        /* try {
           RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null){
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                System.out.println(request.getHeader("tenant"));
            }*/
/*
            JWT jwt = JWTParser.parse(token);
            JWTClaimsSet claimsSet =  jwt.getJWTClaimsSet();
            Map<String, Object> claims =  claimsSet.getClaims();
            if(claims.containsKey("appId")){
                App app = appRepo.findById(claims.get("appId").toString()).block();
                if(app != null && app.getJwkSetUri() != null && !app.getJwkSetUri().isEmpty()){
                    return NimbusJwtDecoder.withJwkSetUri(app.getJwkSetUri()).build();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } */
        return defaultJwtDecoder;
    }

}
