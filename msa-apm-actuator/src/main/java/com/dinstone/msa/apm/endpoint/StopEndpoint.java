
package com.dinstone.msa.apm.endpoint;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;

@Component
@WebEndpoint(id = "stop")
public class StopEndpoint {

    @Autowired
    ApplicationInfoManager applicationInfoManager;

    @ReadOperation
    public Map<String, String> invoke() {
        InstanceInfo info = applicationInfoManager.getInfo();
        InstanceStatus preStatus = info.setStatus(InstanceStatus.OUT_OF_SERVICE);
        return Collections.singletonMap("pre-status", preStatus != null ? preStatus.toString() : null);
    }

}
