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

import org.springframework.stereotype.Component;

import com.dinstone.vertx.web.annotation.Interceptor;
import com.dinstone.vertx.web.annotation.Path;

import io.vertx.ext.web.RoutingContext;

@Component
@Interceptor
public class HelloInterceptor {

	@Path("/*")
	public void intercept(RoutingContext ctx) {
		// System.out.println(ctx.request().path());
		ctx.next();
	}

}