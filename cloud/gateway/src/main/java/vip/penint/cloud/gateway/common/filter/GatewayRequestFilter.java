package vip.penint.cloud.gateway.common.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vip.penint.cloud.common.core.entity.constant.PenintConstant;

/**
 *
 */
@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
public class GatewayRequestFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        byte[] token = Base64Utils.encode((PenintConstant.GATEWAY_TOKEN_VALUE).getBytes());
        String[] headerValues = {new String(token)};
        ServerHttpRequest build = exchange.getRequest().mutate().header(PenintConstant.GATEWAY_TOKEN_HEADER, headerValues).build();
        ServerWebExchange newExchange = exchange.mutate().request(build).build();
        return chain.filter(newExchange);
    }
}
