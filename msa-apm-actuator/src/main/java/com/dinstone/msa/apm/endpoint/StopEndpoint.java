package com.dinstone.msa.apm.endpoint;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

@ConfigurationProperties(prefix = "endpoints.stop")
public class StopEndpoint extends AbstractEndpoint<Map<String, String>> {

	@Autowired
	ApplicationInfoManager applicationInfoManager;

	public StopEndpoint() {
		super("stop", true, true);
	}

	public StopEndpoint(boolean sensitive) {
		super("stop", sensitive);
	}

	public StopEndpoint(boolean sensitive, boolean enabled) {
		super("stop", sensitive, enabled);
	}

	@Override
	public Map<String, String> invoke() {
		InstanceInfo info = applicationInfoManager.getInfo();
		InstanceStatus preStatus = info.setStatus(InstanceStatus.OUT_OF_SERVICE);
		return Collections.singletonMap("pre-status", preStatus != null ? preStatus.toString() : null);
	}

}
