package com.dinstone.mas.config.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigurationController {

	@Value("${book.author:unknown}")
	String bookAuthor;

	@Value("${book.name:unknown}")
	String bookName;

	@Value("${book.category:unknown}")
	String bookCategory;

	@Autowired
	private Environment environment;

	@Autowired
	private BookProperties properties;

	@Autowired
	ApplicationContext applicationContext;

	@GetMapping("/config1")
	public String config() {
		return "bookAuthor=" + bookAuthor + "<br/>bookName=" + bookName + "<br/>bookCategory=" + bookCategory;
	}

	@GetMapping("/config2")
	public String config2() {

		return "env.get('book.author')=" + environment.getProperty("book.author", "unknown")
				+ "<br/>env.get('book.name')=" + environment.getProperty("book.name", "unknown")
				+ "<br/>env.get('book.category')=" + environment.getProperty("book.category", "unknown");

	}

	@GetMapping("/config3")
	public String config3() {
		return "book.Author=" + properties.getAuthor() + "<br/>book.Name=" + properties.getName()
				+ "<br/>book.Category=" + properties.getCategory();
	}

	/**
	 * 注意：@RefreshScope 注解和需要发送 RefreshEvent 的需不在同一个类中，否则可能会产生死锁。
	 * 
	 * @return
	 */
	@GetMapping("/event")
	public String event() {

		applicationContext.publishEvent(new RefreshEvent(this, null, "just for test"));

		return "send RefreshEvent";
	}

}
