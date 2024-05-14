package com.dinstone.msa.gray;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * add gray header to RestTemplate's request
 * 
 * @author dinstone
 *
 */
public class GrayRestRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String grayValue = null;
        if (attributes != null) {
            grayValue = attributes.getRequest().getHeader(GrayConstant.HEADER_LABEL);
        }
        if (GrayConstant.HEADER_VALUE.equalsIgnoreCase(grayValue)) {
			request.getHeaders().set(GrayConstant.HEADER_LABEL, GrayConstant.HEADER_VALUE);
			attributes.setAttribute(GrayConstant.HEADER_LABEL, GrayConstant.HEADER_VALUE, 0);
		}

		return execution.execute(request, body);
	}

}
