package com.reed.jsonp;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
/**
 * spring mvc支持jsonp
 * @author reed
 *例：
 *请求：http://localhost:8080/api/json?callback=test
 *返回：test($json)
 */
@Order(2)
@ControllerAdvice(basePackages = "com.*.api.rest")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
	public JsonpAdvice() {
		super("callback", "jsonp"); // 指定jsonpParameterNames
	}
}
