package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author firoz
 * @since 15/11/16
 */
@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Value("${security.signing-key}")
    private String signingKey;

    @Value("${endpoints.cors.allowed-origins}")
    private String allowedOrigin;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceIds).tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/tt-auth/**", "/env/**", "/metrics/**").permitAll().anyRequest()
                .authenticated();
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         http.cors().and().headers().addHeaderWriter(new HeaderWriter() {
            @Override
            public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
                if(response.getHeader("Access-Control-Allow-Origin")==null || response.getHeader("Access-Control-Allow-Origin").isEmpty()) {
                    response.addHeader("Access-Control-Allow-Origin", allowedOrigin);
                }
                if (request.getMethod().equals("OPTIONS")) {
                    response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                }
            }
        });
    }

//    @Override
//    public void configure(final ResourceServerSecurityConfigurer resources) {
//        resources.resourceId("tt-gateway");
//    }
}
