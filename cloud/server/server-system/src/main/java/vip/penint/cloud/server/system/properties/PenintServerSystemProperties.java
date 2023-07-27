package vip.penint.cloud.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * 
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:server-system.properties"})
@ConfigurationProperties(prefix = "server.system")
public class PenintServerSystemProperties {
    /**
     * 批量插入当批次可插入的最大值
     */
    private Integer batchInsertMaxNum = 1000;
}
