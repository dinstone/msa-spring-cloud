package com.dinstone.msa.swimlane;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * add swimlane header to Feign template's request
 *
 * @author dinstone
 */
public class SwimlaneFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String swimlaneValue = null;
        if (attributes != null) {
            swimlaneValue = attributes.getRequest().getHeader(SwimlaneConstant.SWIMLANE_HEADER);
        }
        if (swimlaneValue != null) {
            template.header(SwimlaneConstant.SWIMLANE_HEADER, swimlaneValue);
            attributes.setAttribute(SwimlaneConstant.SWIMLANE_HEADER, swimlaneValue, RequestAttributes.SCOPE_REQUEST);
        }
    }

}
