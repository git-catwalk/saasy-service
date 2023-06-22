package com.bluntsoftware.saasy_service.config;

import com.bluntsoftware.saasy_service.repository.AppRepo;
import com.bluntsoftware.saasy_service.repository.TenantRepo;
import com.bluntsoftware.saasy_service.utils.AppAwareJwtDecoder;
import com.bluntsoftware.saasy_service.utils.JwtRoleConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;
    private final AppRepo appRepo;
    private final TenantRepo tenantRepo;

    public WebSecurityConfig(AppRepo appRepo, TenantRepo tenantRepo) {
        this.appRepo = appRepo;
        this.tenantRepo = tenantRepo;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/public/**").permitAll()
                .antMatchers("/api/v1/**").authenticated()
                .antMatchers("/rest/**").authenticated()
                .anyRequest()
                .permitAll()
                .and()
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> jwtRoleConverter(){
        return new JwtRoleConverter(tenantRepo);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return new AppAwareJwtDecoder(appRepo, jwkSetUri);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods( Collections.singletonList( "*" ) );
        config.setAllowedOrigins( Collections.singletonList( "*" ) );
        config.setAllowedHeaders( Collections.singletonList( "*" ) );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config );
        return source;
    }
}
