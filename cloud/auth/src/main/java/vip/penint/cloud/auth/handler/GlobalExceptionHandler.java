package vip.penint.cloud.auth.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.penint.cloud.common.core.handler.BaseExceptionHandler;

/**
 *
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {
}
