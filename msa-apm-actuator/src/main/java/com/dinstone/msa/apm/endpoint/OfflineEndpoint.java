package com.dinstone.msa.apm.endpoint;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

@ConfigurationProperties(prefix = "endpoints.offline")
public class OfflineEndpoint extends AbstractEndpoint<Map<String, String>> {

	@Autowired
	ApplicationInfoManager applicationInfoManager;

	public OfflineEndpoint() {
		super("offline");
	}

	public OfflineEndpoint(boolean sensitive) {
		super("offline", sensitive);
	}

	public OfflineEndpoint(boolean sensitive, boolean enabled) {
		super("offline", sensitive, enabled);
	}

	@Override
	public Map<String, String> invoke() {
		InstanceInfo info = applicationInfoManager.getInfo();
		InstanceStatus preStatus = info.setStatus(InstanceStatus.DOWN);
		return Collections.singletonMap("pre-status", preStatus != null ? preStatus.toString() : null);
	}

}
