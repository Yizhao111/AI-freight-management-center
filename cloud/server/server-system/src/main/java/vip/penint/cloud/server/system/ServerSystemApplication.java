package vip.penint.cloud.server.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vip.penint.cloud.common.security.starter.annotation.EnableCloudResourceServer;

/**
 *
 */
@EnableAsync
@EnableFeignClients(basePackages = "vip.penint.cloud")
//@EnableFeignClients
@SpringBootApplication
@EnableCloudResourceServer
@EnableTransactionManagement
@MapperScan("vip.penint.cloud.server.system.mapper")
public class ServerSystemApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerSystemApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
