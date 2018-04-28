package com.dinstone.msa.consumer.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.stereotype.Component;

@Component
public class MyMetricsEndpoint extends AbstractEndpoint<Map<String, Object>> {

	public MyMetricsEndpoint() {
		super("my");
	}

	@Override
	public Map<String, Object> invoke() {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("timer.esp", "1.20");
		return hashMap;
	}

}
