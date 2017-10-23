# Eureka
服务注册、服务发现、服务检查
- eureka server
- eureka client
- eureka server 高可用
- eureka 的自我保护模式
### 注：
1. 在生产环境中，通常会部署一个高可用的Eureka Server集群  
![服务发现架构图](images/服务发现架构图.png)

![Eureka 架构图](images/Eureka架构图.jpeg)

# Ribbon
Ribbon是负载均衡器，用来实现客户端侧的负载均衡

### 脱离Eureka使用Ribbon
需求：未注册到Eureka Server或者非Spring Cloud开发的服务使用Ribbon实现负载均衡
### 注：
1. 一般来说，在生产环境中，各个微服务都会部署多个实例

# Feign
声明式、模板化的HTTP客户端，支持Spring MVC注解，方便调用HTTP API.
 
 ### Feign 的日志
 需求：了解Feign处理请求的具体细节
 
 ### 使用Feign构造多参数请求
 1. GET请求
 ```java
@FeignClient(name = "user")
public interface UserFeignClient {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User get(@RequestParam("id") Long id, @RequestParam("username") String username);
}
```
```java
@FeignClient(name = "user")
public interface UserFeignClient {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User get(@RequestParam java.util.Map<String, Object> map);
}
```
2. POST请求
```java
@FeignClient(name = "user")
public interface UserFeignClient {
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public User post(@RequestBody User user);
}
```
### 注：
1. feignClient中interface的写法要注意，不然接口会掉不通

# Hystrix
一个延迟和容错库，用于隔离访问远程系统、服务或者第三方库，防止级联失败，从而提升系统的可用性与容错性。

### 容错机制要求
- 为网络请求设置超时
- 使用断路器模式

### 容错机制实现
- 包裹请求
- 跳闸机制
- 资源隔离
- 监控
- 回退机制
- 自我修复

### Feign 使用 Hystrix
请务必注意：在Spring Cloud Dalston中，Feign默认是不开启Hystrix的。因此，如使用Dalston请务必额外设置属性：feign.hystrix.enabled=true，否则断路器不会生效。而，Spring Cloud Angel/Brixton/Camden中，Feign默认都是开启Hystrix的。无需设置该属性。

### 通过Fallback Factory检查回退原因

### 为Feign禁用Hystrix
在spring cloud中，只要Hystrix在项目的classpath中，Feign就会使用断路器包裹Feign客户端的所有方法。这样虽然方便，但很多场景下并不需要该功能。
- 为指定Feign客户端禁用Hystrix
```java
// 配置
@Configuration
public class FeignDisableHystrixConfiguration {
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}

// 使用
@FeignClient(name="user", configuration=FeignDisableHystrixConfiguration.class)
public interface UserFeignClient {
    //...
}
```
- 全局禁用Hystrix  
application.yml文件中配置：
```yaml
feign:
  hystrix:
    enabled: false
```

### Hystrix监控
![Hystrix Dashboard](images/dashboard-annoted-circuit.png)

### 使用turbine聚合监控数据
一个聚合Hystrix监控数据的工具，将所有/hystrix.stream端点的数据聚合到一个组合的/turbine.stream中。

- 使用消息中间件收集数据  
某些场景下（例如微服务与Turbine网络不通），则可借助消息中间件实现数据的收集

- 注：在spring cloud Camden SR4中，依赖spring-cloud-starter-turbine不能与spring-cloud-starter-turbine-stream共存，否则启动时会报异常。

# Zuul
是Netflix开源的微服务网关。微服务网关是介于客户端和服务器端之间的中间层，所有的外部请求都会先经过微服务网关。
### 使用微服务网关的优点
- 易于监控
- 易于认证
- 减少了客户端与各个微服务之间的交互次数
### Zuul的核心是一些过滤器，可实现的功能
- 身份认证与安全
- 审查与监控
- 动态路由
- 压力测试
- 负载分配
- 静态响应处理多区域弹性
### zuul的路由规则
http://ZUUL_HOST:ZUUL_PORT/微服务在Eureka上的serviceId/** 会被转发到serviceId对应的微服务。
### Zuul的路由端点
/routes  
- GET请求访问：返回Zuul当前映射的路由列表
- POST请求访问：强制刷新Zuul当前映射的路由列表（尽管路由会自动刷新）
### 路由配置
- 自定义指定微服务的访问路径（zuul.routes.指定微服务的serviceId = 指定路径）
```yaml
zuul:
  routes:
    microservice-provicer-user: /user/**
```
- 同时指定微服务的serviceId和对应的路径
```yaml
zuul:
  routes:
    # 该配置方式中，user-route只是给路由一个名称，可以任意起名
    user-route:
      service-id: microservice-provicer-user
      path: /user/**
```
### Zuul的安全与Header
一般来说，可在同一个系统中的服务之间共享Header。不过应尽量防止让一些敏感的Herder外泄。因此，在很多场景下，需要通过为路由指定一系列敏感Header列表。如：
```yaml
zuul:
  routes:
    microservice-provider-user:
      url: https://downstream
      path: /users/**
      sensitive-headers: Cookie,Set-Cookie,Authorization
```
### Zuul 过滤器
1 过滤器类型与请求生命周期  
- PRE
- ROUTING
- POST
- ERROR  
![Zuul请求的生命周期](images/zuul-request-lifecycle.png)   
2 继承ZuulFilter需要实现的方法  
- filterType()：返回过滤器的类型
- filterOrder()：返回毅哥int值来指定过滤器的执行顺序，不同的过滤器允许返回相同的数字
- shouldFilter()：返回一个boolean值来判断该过滤器是否执行，true表示执行，false表示不执行
- run()：过滤器的具体逻辑
### 为Zuul添加回退
实现ZuulFallbackProvider接口

### Zuul的高可用
- 方式一：Zuul客户端也注册到Eureka Server上
只须将多个Zuul节点注册到Eureka Server上，就可实现Zuul的高可用  
**注：Zuul客户端是指发送请求的客户端**
- 方式二：Zuul客户端为注册到Eureka Server上
现实中，这种场景往往更常见。例如，Zuul客户端是一个手机APP--不可能让所有手机终端都注册到Eureka Server上。这种情况下，可借助一个额外的负载均衡器来实现Zuul的高可用，例如**Nginx、HAProxy、F5**等。

### 使用Sidecar整合非JVM微服务
- 非JVM微服务调用注册在Eureka Server上的JVM微服务
- JVM微服务调用非JVM微服务的接口
- Sidecar与Node.js微服务分离部署

1 方法一：
```yaml
eureka:
  instance:
    hostname: 非JVM微服务的hostname
```
2 方法二：
```yaml
sidecar:
  hostname: 非JVM微服务的hostname
  ip-address: 非JVM微服务的IP地址
```

### 使用Zuul聚合微服务

### 注：
1. 目前，zuul使用的默认HTTP客户端是Apache HTTP Client，若使用RestClient，需配置ribbon.restclient.enabled=true；若使用okhttp3.OkHttpClient,需配置ribbon.okhttp.enabled=true。
2. Zuul可以使用Ribbon达到负载均衡的效果
3. Zuul整合了Hystrix，并实现监控
4. Zuul的Hystrix监控的粒度是微服务，而不是某个API
5. 所有经过Zuul的请求，都会被Hystrix保护起来

# Config  
spring cloud config 为分布式系统外部化配置提供了服务器端和客户端的支持，包含：  
- Config Server
- Config Client

微服务配置管理需求：
- 集中管理配置
- 不同环境不同配置（开发、测试、预发布、生产等）
- 运行期间可动态调整
- 配置修改后可自动更新

### 引导上下文
引导上下文负责从配置服务器加载配置属性，以及解密外部配置文件中的属性。和主应用程序加载application.*(yml or properties)中的属性不同，引导上下文加载bootstrap.*中的属性。配置在bootstrap.*中的属性有更高的优先级。

### 配置内容的加解密
- 安装JCE  
地址：www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
安装：将JDK/jre/lib/security 目录中的两个jar文件替换为压缩包中的jar文件
- 对称加密
```yaml
encrypt:
  key: foo # 设置对称密码
```
```
测试：（postman）
# 加密：
POST localhost:8080/encrypt     Body Raw Text
mysecret

# 解密：
POST localhost:8080/decrypt     Body Raw Text
fb4759f094e4e9e12b8c14f3d8504b395e1055da9bc586572d4395fad9adef6a
```
**注：将spring cloud的版本由默认的 Dalston.SR4 改为 Camden.SR4版本，否则会报异常“No key was installed for encryption service”**
存储加密的内容：  
创建一个encryption.yml文件
```yaml
spring:
  datasource:
    username: dbuser
    password: '{cipher}a54323bb30ff3d5e7544108a29c940c87c8a25d057cd77fc96b76b1fbe231d8a'
```  
使用http://localhost:8080/encryption-default.yml获取结果  
如果配置文件为encryption.properties  
```properties
spring.datasource.username=dbuser
spring.datasource.password={cipher}a54323bb30ff3d5e7544108a29c940c87c8a25d057cd77fc96b76b1fbe231d8a
```  
一些场景下，想要让Config Server直接返回密文本身，而非解密后的内容，可设置spring.cloud.config.server.encry.enabled=false，这时可由Config Client自行解密
# Docker
开源的容器引擎，有助于更快的交付应用  
- Docker daemon（Docker守护进程）
- Client（Docker客户端）
- Images（Docker镜像）
- Container（容器）
- Registry（注册中心）

### 安装Docker

### 配置镜像加速器
- Docker 版本在 1.12 或更高:  
```
1. 创建或修改 /etc/docker/daemon.json 文件
vim /etc/docker/daemon.json
{
    "registry-mirrors": [
        "http://f1615e4e.m.daocloud.io"
    ],
    "insecure-registries": []
}
2. 重启docker服务
systemctl restart docker
```

### Docker常用命令
- Docker镜像常用命令
```
docker search imageName         # 搜索镜像
docker pull imageName           # 拉取镜像
docker images                   # 列出镜像
docker rmi imageName            # 删除镜像
docker rmi -f $(docker images)  # 删除所有镜像
```
- Docker容器常用命令
```
# 新建并启动容器（-d：后台运行；-p：指定端口映射；/bin/echo：终端打印字符串）
docker run [-d -p 宿主机端口:容器端口] imageName [/bin/echo 'Hello Docker']
docker ps                   # 列出容器
docker stop containerId     # 停止容器
docker kill containerId     # 强制停止容器
docker start containerId    # 启动已停止的容器
docker restart containerId  # 重启容器
# 进入容器
docker inspect --format "{{.State.Pid}}" containerId # 获取容器第一个进程的PID
nsenter --target "$PID" --mount --uts --ipc --net --pid #进入容器
docker rm containerId           # 删除已停止的容器 
docker r -f $(docker ps -a -q)  # 删除所有容器
```

### Dockerfile构建镜像

### Docker Compose编配微服务
使用Compose管理容器

# 常见问题
## 1. 已停止的微服务节点注销慢或不注销
- 问题描述：  
在开发环境下，希望Eureka Server能迅速有效地注销已停止的微服务实例。由于Eureka Server清理无效节点周期长（默认90秒），以及自我保护模式等原因，可能会遇到微服务注销慢甚至不注销的问题。
- 解决方案：  
```yaml
# Eureka Server 配置
eureka:
  server:
    enable-self-preservation: false # 关闭自我保护
    eviction-interval-timer-in-ms: 4000 # 清理间隔（单位：ms，默认：60*1000）
    
# Eureka Client 配置
eureka:
  client:
    healthcheck:
      enabled: true # 开启健康检查
  instance:
    lease-expiration-duration-in-seconds: 30  # 续约更新时间间隔（默认30s）
    lease-renewal-interval-in-seconds: 10 # 续约到期时间（默认90s）
```
- 注：
这些配置仅建议在开发或测试时使用，生产环境建议坚持使用默认值。