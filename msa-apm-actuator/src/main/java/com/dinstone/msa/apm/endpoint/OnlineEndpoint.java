package com.dinstone.msa.apm.endpoint;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

@ConfigurationProperties(prefix = "endpoints.online")
public class OnlineEndpoint extends AbstractEndpoint<Map<String, String>> {

	@Autowired
	ApplicationInfoManager applicationInfoManager;

	public OnlineEndpoint() {
		super("online");
	}

	public OnlineEndpoint(boolean sensitive) {
		super("online", sensitive);
	}

	public OnlineEndpoint(boolean sensitive, boolean enabled) {
		super("online", sensitive, enabled);
	}

	@Override
	public Map<String, String> invoke() {
		InstanceInfo info = applicationInfoManager.getInfo();
		InstanceStatus preStatus = info.setStatus(InstanceStatus.UP);
		return Collections.singletonMap("pre-status", preStatus != null ? preStatus.toString() : null);
	}

}
