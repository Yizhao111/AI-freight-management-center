package vip.penint.cloud.server.ques;

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
@MapperScan("vip.penint.cloud.server.ques.mapper")
public class ServerQuesApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerQuesApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
