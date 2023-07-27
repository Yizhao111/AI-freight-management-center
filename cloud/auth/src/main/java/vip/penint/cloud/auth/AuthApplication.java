package vip.penint.cloud.auth;

import org.springframework.cloud.openfeign.EnableFeignClients;
import vip.penint.cloud.common.security.starter.annotation.EnableCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 *
 */
@EnableFeignClients
@SpringBootApplication
@EnableRedisHttpSession
@EnableCloudResourceServer
@MapperScan("vip.penint.cloud.auth.mapper")
public class AuthApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
