## Spring Cloud FQA

Q: config动态配置有哪几种方式？

A: 主要有三种方式配置可以动态刷新：（高版本的需要引入依赖：spring-cloud-starter-bootstrap）

   1. Environment获取配置，可以动态更新；
   2. @Value 需要配合@RefreshScope 才能获取到动态配置；
   3. @ConfigurationProperties 可以获取到动态配置。

Q: 配置项中的所以字段都会自动应用吗？

A: 不是的。只有使用了上面的三种方式获取配置的组件才能享受配置的自动更新特性。比如server.port的变动是不会起作用的。

Q: 怎么实现配置刷新？

A: 需要调用ContextRefresher.refresh()方法。
   1. 手动刷新，当配置文件变更后，手动调用配置客户端的接口，接口中发送ApplicationContext.publishEvent(RefreshEvent)事件，或者使用/actuator/refresh端点触发ContextRefresher.refresh()方法调用。
   2. 自动刷新，当发现配置文件变更后，自动发送ApplicationContext.publishEvent(RefreshEvent)事件。或者使用 spring cloud bus消费配置变更消息来触发更新。

Q: Gateway可以动态变更路由信息吗？

A: 可以通过动态配置刷新路由列表，实时生效，不会对访问造成影响。

Q: Spring cloud 核心组件选型？

A: 核心组件选型

   1. API-Gateway， Spring Cloud Gateway
   2. 服务注册，Consul
   3. 服务配置，Consul
   4. 服务调用，RestTemplate、OpenFeign、Spring Cloud Loadbalancer
   5. 流量治理，Spring Cloud Circuit Breaker
   6. 可观测性，Spring Cloud Sleuth、Spring Actuator + Prometheus
