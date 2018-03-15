package com.dinstone.msa.consumer.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dinstone.msa.model.User;

@FeignClient(value = "user-provider", path = "/user", configuration = FeignClientConfig.class, fallbackFactory = UserFallbcakFactory.class)
public interface UserClientService {

	@RequestMapping(method = RequestMethod.GET, value = "/get/{uid}")
	User get(@PathVariable("uid") long uid);

	@RequestMapping(method = RequestMethod.GET, value = "/list")
	List<User> list();

}
