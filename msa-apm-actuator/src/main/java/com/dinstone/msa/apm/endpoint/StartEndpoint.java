package com.dinstone.msa.apm.endpoint;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

@ConfigurationProperties(prefix = "endpoints.start")
public class StartEndpoint extends AbstractEndpoint<Map<String, String>> {

	@Autowired
	ApplicationInfoManager applicationInfoManager;

	public StartEndpoint() {
		super("start", true, true);
	}

	public StartEndpoint(boolean sensitive) {
		super("start", sensitive);
	}

	public StartEndpoint(boolean sensitive, boolean enabled) {
		super("start", sensitive, enabled);
	}

	@Override
	public Map<String, String> invoke() {
		InstanceInfo info = applicationInfoManager.getInfo();
		InstanceStatus preStatus = info.setStatus(InstanceStatus.STARTING);
		return Collections.singletonMap("pre-status", preStatus != null ? preStatus.toString() : null);
	}

}
