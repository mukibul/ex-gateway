package com.example.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author mukibul
 * @since 24/02/18.
 */
@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class CorsCustomization {
    /*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://www.toppertalks.com");
        config.addAllowedOrigin("http://localhost:4000");

        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        source.registerCorsConfiguration("*//**", config);
        CorsFilter corsFilter=new CorsFilter(source);


        return corsFilter;
    }
*/
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        /*config.addAllowedOrigin("http://localhost:4000");
        config.addAllowedOrigin("https://www.toppertalks.com");
        config.addAllowedOrigin("https://toppertalks.com");*/
        //config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
