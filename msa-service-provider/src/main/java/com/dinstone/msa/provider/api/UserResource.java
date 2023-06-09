package com.dinstone.msa.provider.api;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String gray = request.getHeader("gray");

		LOG.info("get user with {}, gray={}", uid, gray);
		return userService.findById(uid);
	}

	@GetMapping("/list")
	public List<User> list() {
		LOG.info("list users");
		return userService.findAll();
	}

	@GetMapping("/slow")
	public String slow() {
		LOG.info("slow service");
		int r = new Random().nextInt((3 - 1) + 1) + 1;
		if (r == 3) {
			// try {
			// Thread.sleep(1100);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}

		return "" + r;
	}
}
