Spring Cloud Gateway 根据作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：
GatewayFilter : 需要通过 spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上，或通过spring.cloud.default-filters配置，作用在所有路由上。
GlobalFilter  : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。

