/*
 * Copyright (C) 2016~2018 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dinstone.msa.vertx.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import com.dinstone.vertx.web.annotation.Consumes;
import com.dinstone.vertx.web.annotation.Get;
import com.dinstone.vertx.web.annotation.Handler;
import com.dinstone.vertx.web.annotation.Path;
import com.dinstone.vertx.web.annotation.Post;
import com.dinstone.vertx.web.annotation.Produces;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@Component
@Handler
@Path("/hello")
public class HelloHandler {

	// @Value("${foo}")
	String foo;

	@Autowired
	private LoadBalancerClient balancerClient;

	@Autowired
	private DiscoveryClient discoveryClient;

//	@Value("${spring.application.name}")
	private String applicationName = "config-server";

	@Get
	public void hi(RoutingContext ctx) {
		ctx.response().end(Json.encodePrettily(balancerClient.choose(applicationName)));
	}

	@Get
	public void g(RoutingContext ctx) {
		ctx.response().end(Json.encodePrettily(discoveryClient.getInstances(applicationName)));
	}

	@Get("/g/:name")
	public void hello(RoutingContext ctx) {
		String message = "hello";
		String name = ctx.request().getParam("name");
		if (name != null) {
			message += " " + name;
		}

		JsonObject json = new JsonObject().put("message", message);
		ctx.response().end(json.encode());
	}

	@Post("/p")
	@Produces("text/plain")
	@Consumes("text/json")
	public void post(RoutingContext ctx) {
		ctx.request().bodyHandler(rs -> {
			String content = rs.toJsonObject().getString("content");
			ctx.response().end("Hello " + content + "!");
		});
	}
}