package com.dinstone.msa.gray;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * for feign and header
 * 
 * @author dinstone
 *
 */
public class GrayFeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String gray = attributes.getRequest().getHeader(GrayConstant.GRAY_LABEL);
		if (GrayConstant.GRAY_VALUE.equalsIgnoreCase(gray)) {
			template.header(GrayConstant.GRAY_LABEL, GrayConstant.GRAY_VALUE);
			attributes.setAttribute(GrayConstant.GRAY_LABEL, GrayConstant.GRAY_VALUE, RequestAttributes.SCOPE_REQUEST);
		}
	}

}
