package com.dinstone.msa.swimlane;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * add swimlane header to RestTemplate's request
 *
 * @author dinstone
 */
public class SwimlaneRestRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String swimlaneValue = null;
        if (attributes != null) {
            swimlaneValue = attributes.getRequest().getHeader(SwimlaneConstant.HEADER_LABEL);
        }
        if (swimlaneValue != null) {
            request.getHeaders().set(SwimlaneConstant.HEADER_LABEL, swimlaneValue);
            attributes.setAttribute(SwimlaneConstant.HEADER_LABEL, swimlaneValue, RequestAttributes.SCOPE_REQUEST);
        }

        return execution.execute(request, body);
    }

}
