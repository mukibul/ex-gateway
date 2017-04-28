package com.skidata.ssp.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author firoz
 * @since 17/10/16
 */
@SpringBootApplication
@EnableZuulProxy
public class GatewayStarter {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayStarter.class).web(true).run(args);
    }
}
