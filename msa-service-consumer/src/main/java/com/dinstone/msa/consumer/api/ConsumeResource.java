package com.dinstone.msa.consumer.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dinstone.msa.consumer.service.UserClientService;
import com.dinstone.msa.model.User;

@RestController
@RequestMapping("/consume")
public class ConsumeResource {

	private static final Logger log = LoggerFactory.getLogger(ConsumeResource.class);

	@Autowired
	private UserClientService userClientService;

	@Autowired
	RestTemplateService restTemplateService;

	@GetMapping("/get/{uid}")
	public User get(@PathVariable("uid") long uid) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String gray = request.getHeader("swimlane");

		log.info("get access: {}", gray);
		return userClientService.get(uid);
	}

	@GetMapping("/list")
	public List<User> list() {
		log.info("list access");
		return userClientService.list();
	}

	@GetMapping("/dc")
	public String dc() {
		log.info("dc access");
		return restTemplateService.consumer();
	}

	@GetMapping("/breaker")
	public String breaker() {
		log.info("breaker access");
		return restTemplateService.breaker();
	}

	@Component
	static class RestTemplateService {

		@Autowired
		RestTemplate restTemplate;

		public String breaker() {
			return restTemplate.getForObject("http://user-provider/user/slow", String.class);
		}

		public String consumer() {
			return restTemplate.getForObject("http://eureka-client/dc", String.class);
		}

		public String fallback() {
			return "fallback";
		}

	}
}
