package net.bncloud.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * 类名称:    HttpFilter
 * 类描述:    http请求拦截器
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/23 11:35 上午
 */
@Component
public class HttpFilter implements GlobalFilter {
	private static final Logger logger = LoggerFactory.getLogger(HttpFilter.class);
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("进入网关");
		ServerHttpRequest req = exchange.getRequest();
		HttpHeaders httpHeaders = req.getHeaders();
		ServerHttpRequest.Builder requestBuilder = req.mutate();
		// 删除Accept-Language请求头
		requestBuilder.headers(k -> k.remove("Accept-Language"));
		ServerHttpRequest request = requestBuilder.build();
		exchange.mutate().request(request).build();
		logger.info("删除Accept-Language完成");
		return chain.filter(exchange);
	}
}
