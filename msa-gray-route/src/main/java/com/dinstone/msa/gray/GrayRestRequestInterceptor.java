package com.dinstone.msa.gray;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * for to add gray header for RestTemplate's request
 * 
 * @author dinstone
 *
 */
public class GrayRestRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String gray = attributes.getRequest().getHeader(GrayConstant.GRAY_LABEL);
		if (GrayConstant.GRAY_VALUE.equalsIgnoreCase(gray)) {
			request.getHeaders().set(GrayConstant.GRAY_LABEL, GrayConstant.GRAY_VALUE);
			attributes.setAttribute(GrayConstant.GRAY_LABEL, GrayConstant.GRAY_VALUE, 0);
		}

		return execution.execute(request, body);
	}

}
