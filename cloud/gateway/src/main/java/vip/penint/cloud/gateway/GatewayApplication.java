package vip.penint.cloud.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
