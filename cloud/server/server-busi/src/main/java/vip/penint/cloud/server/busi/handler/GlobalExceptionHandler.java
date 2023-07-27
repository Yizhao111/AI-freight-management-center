package vip.penint.cloud.server.busi.handler;

import vip.penint.cloud.common.core.handler.BaseExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {
}
