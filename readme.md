## 架构选型 FQA

### 1 : config动态配置有哪几种方式？

主要有三种方式配置可以动态刷新：（高版本的需要引入依赖：spring-cloud-starter-bootstrap）

   1. Environment获取配置，可以动态更新；
   2. @Value 需要配合@RefreshScope 才能获取到动态配置；
   3. @ConfigurationProperties 可以获取到动态配置。

### 2 : 配置项中的所有字段都会自动应用吗？

不是的。只有使用了上面的三种方式获取配置的组件才能享受配置的自动更新特性。比如server.port的变动是不会起作用的。

### 3 : 怎么实现配置刷新？

需要调用ContextRefresher.refresh()方法。

   1. 手动刷新，当配置文件变更后，手动调用配置客户端的接口，接口中发送ApplicationContext.publishEvent(RefreshEvent)事件，或者使用/actuator/refresh端点触发ContextRefresher.refresh()方法调用。
   2. 自动刷新，当发现配置文件变更后，自动发送ApplicationContext.publishEvent(RefreshEvent)事件。或者使用 spring cloud bus消费配置变更消息来触发更新。

#### 4 : Gateway可以动态变更路由信息吗？

可以通过动态配置刷新路由列表，实时生效，不会对访问造成影响。

### 5 : Spring cloud 核心组件选型？

核心组件选型：
   1. 服务网关: Spring Cloud Gateway
   2. 服务管理: Consul
   3. 配置管理: Consul
   4. 服务调用: RestTemplate、OpenFeign、Spring Cloud Loadbalancer
   5. 流量治理: Spring Cloud Circuit Breaker
   6. 可观测性: Spring Cloud Sleuth、Spring Actuator、Prometheus

### 6 : 如何实现开发、测试环境的多分支协作， 以及生产环境的灰度发布？

1. 每个服务定义3组元数据：环境（Environment）、泳道（Swimlane）、版本（version）
* Environment：develop、testing、product
* Swimlane：stable、gray; base、feature
* Version：xx.yy.zz 由主版本号、次版本号和修订号三部分组成，例如：1.20.13

2. 定义路由组件，增强服务路由能力，支持多分支协助的开发、测试和生产环境的灰度发布。


## 快速开始 FQA

Q: 如何构建spring cloud基础设施？

A: docker 环境构建是最快，最省力的方式。

1. [prometheus & pushgateway](https://blog.csdn.net/qq_36120342/article/details/119758402)

prometheus.yml

```
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "rules/alert_rules.yml"
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: prometheus
    static_configs:
    - targets: ['192.168.1.120:9090']
  - job_name: pushgateway
    static_configs:
      - targets: ['192.168.1.120:9091']
        labels:
          instance: pushgateway
```

```
docker run -d -p 9091:9091 \
 -v /usr/share/zoneinfo/Asia/Shanghai:/etc/localtime\
 -v $HOME/prometheus/data:/prometheus-data \
 --name pushgateway prom/pushgateway:latest

 docker run -d -p 9090:9090 \
 -v /usr/share/zoneinfo/Asia/Shanghai:/etc/localtime\
 -v $HOME/prometheus/config:/etc/prometheus \
 --name prometheus prom/prometheus:latest '--config.file=/etc/prometheus/prometheus.yml'
```

2. consul

```
docker run -d --restart=unless-stopped --name consul -p 8500:8500 -v $HOME/consul:/consul/data consul agent -server -bootstrap -ui -client='0.0.0.0'
```
