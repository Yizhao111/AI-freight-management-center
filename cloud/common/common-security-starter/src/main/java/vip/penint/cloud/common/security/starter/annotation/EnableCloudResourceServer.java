package vip.penint.cloud.common.security.starter.annotation;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import vip.penint.cloud.common.security.starter.configure.CloudResourceServerConfigure;
import vip.penint.cloud.common.security.starter.feign.FeignAutoConfiguration;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
@Documented
// 开启线程异步执行
@EnableAsync
// 自动加载类
@Import({CloudResourceServerConfigure.class, FeignAutoConfiguration.class})
public @interface EnableCloudResourceServer {
}
