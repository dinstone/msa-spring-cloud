package com.dinstone.msa.provider.api;

import java.util.List;

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

	@Autowired
	private UserService userService;

	@GetMapping("/get/{uid}")
	public User get(@PathVariable("uid") int uid) {
		return userService.findById(uid);
	}

	@GetMapping("/list")
	public List<User> list() {
		return userService.findAll();
	}
}
