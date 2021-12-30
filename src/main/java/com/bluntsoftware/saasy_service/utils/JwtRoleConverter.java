package com.bluntsoftware.saasy_service.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = new ArrayList<>();
        Map<String, Object> claims = jwt.getClaims();

        String issuer = claims.get("iss").toString();
        String realm = issuer.substring(issuer.lastIndexOf("/") + 1);

        if(claims.containsKey("roles") && realm.equalsIgnoreCase("saasy")) {
            roles = (List<String>) claims.get("roles");
        }else{
            roles.add("TENANT_USER");
        }

        return  roles.stream()
                .map(roleName -> "ROLE_" + roleName.replace("/","").toUpperCase())
                .collect(Collectors.toList()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
