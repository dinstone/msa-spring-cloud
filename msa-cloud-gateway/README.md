Spring Cloud Gateway 根据Filter的作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：
- GatewayFilter : 局部过滤器，需要通过 spring.cloud.routes.filters 配置在具体路由下，只作用在指定路由上，或通过spring.cloud.default-filters配置，作用在所有路由上。
- GlobalFilter  : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，不需要配置，系统初始化时加载，并作用在每个路由上，它为请求路由的URI转换为真实业务服务请求地址的核心过滤器。

Server和Manager 如何启动？
ReactiveWebServerApplicationContext -> NettyReactiveWebServerFactory -> WebServerManager 创建Server和Manager

怎么发布反应式RestAPI？
可以使用 RestController 和 Mono 来定义RestAPI，Webflux会自动发布。

- reactive web 请求处理流程：
WebHandler（reactive.DispatcherHandler）-> HandlerMapping -> find handler -> HandlerAdapter -> invoke handler -> HandlerResult

- 基于 @RestController 的Rest API 处理流程：
RequestMappingHandlerMapping -> find handler (HandlerMethod) -> RequestMappingHandlerAdapter -> invoke handler

- 基于 Predicate 的 Route 处理流程：
RoutePredicateHandlerMapping -> find handler (FilteringWebHandler) -> SimpleHandlerAdapter -> invoke handler

Filter的加载和扩展？
GatewayFilter需要定义GatewayFilterFactory，然后在路由中配置。之后由RouteDefinitionRouteLocator构建Route时，创建GatewayFilter。
GlobalFilter 需要定义实例并添加到spring容器中，这样才能被FilteringWebHandler收集到，然后构建 filter chain 执行调用。
其实这里可以做性能优化，将filter chain的构建放在Route 的创建过程中，这样就不用每次请求的时候反复的创建chain了，当然需要对chain的数据结构做变更。

动态路由的实现机制？
路由查找过程：RouteLocator -> RouteDefinitionLocator

CachingRouteLocator -> CompositeRouteLocator -> RouteDefinitionRouteLocator -> CompositeRouteDefinitionLocator -> PropertiesRouteDefinitionLocator,InMemoryRouteDefinitionRepository

路由定义由 RouteDefinitionLocator 来加载，当然也可以从 RouteDefinitionRepository 来加载。
- 常用的 PropertiesRouteDefinitionLocator 从配置文件中加载路由定义，当变更配置后需要刷新上下文才能实现动态路由。 
- 而 DiscoveryClientRouteDefinitionLocator 从注册中心发现服务来构建路由定义，从而实现服务的动态路由。
- InMemoryRouteDefinitionRepository 是将路由定义保存在内存中，可以从内存中加载路由定义来实现动态路由。
- RedisRouteDefinitionRepository 是将路由定义保存在Redis中，可以从Redis中加载路由定义来实现动态路由。
当前的实现都是基于全局的动态路由，一个路由的变更需要全部的路由定义加载，对于路由定义很多的场景，发布很慢。因此需要一种机制来实现增量发布，针对特定路由的动态加载。
