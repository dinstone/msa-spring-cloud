package com.dinstone.msa.consumer.endpoint;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

import com.codahale.metrics.Metric;
import com.codahale.metrics.SharedMetricRegistries;
import com.dinstone.measure.config.MetricConfig;

public class MeasureEndpoint extends AbstractEndpoint<Map<String, Metric>> {

	@Autowired
	private MetricConfig metricConfig;

	public MeasureEndpoint() {
		super("measure");
	}

	@Override
	public Map<String, Metric> invoke() {
		String registryName = metricConfig.getMetricRegistryName();
		return SharedMetricRegistries.getOrCreate(registryName).getMetrics();
	}

}
