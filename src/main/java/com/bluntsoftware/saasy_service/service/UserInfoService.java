package com.bluntsoftware.saasy_service.service;

import com.bluntsoftware.saasy_service.model.App;
import com.bluntsoftware.saasy_service.model.Roles;
import com.bluntsoftware.saasy_service.model.User;
import com.bluntsoftware.saasy_service.repository.AppRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInfoService {
    private final AppRepo appRepo;

    public UserInfoService(AppRepo appRepo) {
        this.appRepo = appRepo;
    }

    public User getLoggedInUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Jwt) {
            return getUser((Jwt)principal);
        }
        return null;
    }

    public  Collection<GrantedAuthority> getRoles(Jwt jwt){
        List<String> roles = new ArrayList<>();
        Map<String, Object> claims = jwt.getClaims();
        //Are you a tenant or a tenant user ?
        if(claims.containsKey("appId")) {
            App app = appRepo.findById(claims.get("appId").toString()).block();
            System.out.print(app);
        }

        if(claims.containsKey("roles")) {
            roles = (List<String>) claims.get("roles");
        }

        return  roles.stream()
                .map(roleName -> "ROLE_" + roleName.replace("/","").toUpperCase())
                .collect(Collectors.toList()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public User getUser(Jwt jwt){
        return User.builder()
                .username(jwt.getClaimAsString("preferred_username"))
                .email(jwt.getClaimAsString("email"))
                .name(jwt.getClaimAsString("name"))
                .roles(getRoles(jwt).stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())).build();
    }

    public boolean hasRole(Roles role) {
        return getLoggedInUser().getRoles().contains(role.getRoleName());
    }

    public boolean isAdmin(){
        return hasRole(Roles.ADMIN);
    }

    public boolean isUser(){
        return hasRole(Roles.USER);
    }
}
