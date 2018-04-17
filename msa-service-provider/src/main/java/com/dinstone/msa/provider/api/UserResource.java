package com.dinstone.msa.provider.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dinstone.msa.model.User;
import com.dinstone.msa.provider.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

	private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	@GetMapping("/get/{uid}")
	public User get(@PathVariable("uid") int uid) {
		LOG.info("get user with {}", uid);
		return userService.findById(uid);
	}

	@GetMapping("/list")
	public List<User> list() {
		LOG.info("list users");
		return userService.findAll();
	}
}
