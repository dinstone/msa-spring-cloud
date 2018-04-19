package com.dinstone.msa.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanReporter;

public class Slf4jSpanReporter implements SpanReporter {

	private static final Logger LOG = LoggerFactory.getLogger(Slf4jSpanReporter.class);

	@Override
	public void report(Span span) {
		LOG.info(span.toString());
	}

}
