package com.dinstone.msa.gray;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * add gray header to Feign template's request
 *
 * @author dinstone
 */
public class GrayFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String grayValue = null;
        if (attributes != null) {
            grayValue = attributes.getRequest().getHeader(GrayConstant.HEADER_LABEL);
        }
        if (GrayConstant.HEADER_VALUE.equalsIgnoreCase(grayValue)) {
            template.header(GrayConstant.HEADER_LABEL, GrayConstant.HEADER_VALUE);
            attributes.setAttribute(GrayConstant.HEADER_LABEL, GrayConstant.HEADER_VALUE, RequestAttributes.SCOPE_REQUEST);
        }
    }

}
