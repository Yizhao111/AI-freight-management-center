package vip.penint.cloud.server.busi;

import vip.penint.cloud.common.security.starter.annotation.EnableCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@EnableFeignClients
@SpringBootApplication
@EnableCloudResourceServer
@EnableTransactionManagement
@MapperScan("vip.penint.cloud.server.busi.mapper")
public class ServerBusiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerBusiApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
