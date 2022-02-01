
# Eureka
- `유레카 서버`는 `마이크로 서비스`가 `등록`되거나 `삭제`될 때 `자동으로 감지`하는 역할을 수행
- `Service Discovery` 역할을 수행. `전화번호부 역할`

## Eureka Server
![eureka_dashboard](/images/eureka_dashboard.png)
- 유레카 서버를 구동시키면 클라이언트들의 등록 상태를 확인하는 대시보드를 제공한다.
- 유레카 서버 이름은 `spring.application.name`에서 설정한다. 
- 유레카 서버는 다른 서버들보다 `먼저 실행`되어야 함.
- register-with-eureka 레지스트리에 자신을 등록할지 여부 
- fetch-registry 레지스트리에 있는 정보를 가져올지 여부 

## Eureka Client 
![eureka_add_client](/images/eureka_add_client.png)
- 유레카 클라이언트를 등록하면 대시보드에 클라이언드 정보가 추가된다. 
- defaultZone은 유레카 클라이언트가 접속할 주소 정보 
- prefer-ip-address는 호스트 이름 대신 ip 주소로 통신 


# Zuul
![api_gateway](/images/api_gateway.png)
- Zuul은 `API Gateway`. 
- `클라이언트가 요청을 서버에게 직접하는게 아니라 zuul을 통해 요청함`. 일종의 `프록시`역할
- 클라이언트가 zuul대신 서버를 직접 호출하면 서버의 수정사항이 발생했을 때, 수정해야하는 단점이 있음.  
- `서비스의 내부 동작은 숨기고 클라이언트의 요청을 적절하게 응답해줌.` 
- 게이트웨이 서비스를 사용하면 인증 및 권한, 부하 분산, 로깅(ELK 대신), IP 허용 목록 추가 등 
- `spring boot 2.4.x` 부터 지원하지 않음. `zuul` 대신 `Spring Cloud Gateway`를 권고.
- `zuul 1`은 `동기` 방식을 지원, `zuul 2`는 `비동기` 방식을 지원함.
- `하지만 zuul 2 방식은 spring에서 채택되지 못하고 spring은 spring-cloud-gateway를 만들어서 사용`
- zuul 사용을 위해 spring downgrade 
  - spring boot 2.6.3 -> 2.3.9.RELEASE. 
  - springCloudVersion 2021.0.0 -> Hoxton.SR1

## zuul 적용 
- gradle dependency. 
  ```
  implementation('org.springframework.cloud:spring-cloud-starter-netflix-zuul') 
  ```
- application에 `@EnableZuulProxy` 어노테이션 추가
  ``` 
  @SpringBootApplication
  @EnableEurekaClient
  @EnableZuulProxy
  public class ZuulApplication {
    public static void main(String[] args) {
      SpringApplication.run(ZuulApplication.class, args);
    }
  }
  ```
- application.yml에 라우팅할 서비스 추가 
  ```
  zuul:
    routes:
      member:
        path: /member-service/**
        //url: http://localhost:8081
        serviceId: member-service
      eureka-client:
        path: /eureka-client/**
        url: http://localhost:8888
    ```
- `유레카`를 쓰고있다면 `url`대신 `serviceId` 속성을 사용할 수 있다. 

## 클라이언트 사이드 로드밸런싱
- `zuul`은 `클라이언트사이드 로드 밸런싱`을 제공한다. 
- 보통 로드밸런싱은 서버 앞에 L4같은 하드웨어 장치인 스위치를 둬서 로드밸런싱을 한다.
- zuul을 사용하게 되면 별도의 장비 없이 클라이언트쪽에서, 즉 `서버를 호출하는 클라이언트 측에서 로드밸런싱을 처리할 수 있다.`
- `zuul`은 `로드 밸런서`로 `netflix-ribbon`을 사용한다. `ribbon`은 내부적으로 `eureka 서버`를 통해 `마이크로 서비스 정보`를 얻어온다.
- `RestTemplate`에도 로드밸런싱을 적용할 수 있다. 
  ``` 
  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
        return new RestTemplate();
  }
  ```
- RestTemplate에 @LoadBalanced 어노테이션을 붙이고, 호출 url을 다음과 같이 바꿀 수 있다.    
  ```
  //    private static final String ORDER_URL = "http://localhost:8000/order-service/%s/orders";
  private static final String ORDER_URL = "http://order-service/%s/orders"; 
  ```
  

## zuul filter
![zuul_filter](/images/zuul_filter.png)
- 필터를 등록하고싶으면 `ZuulFilter`를 상속받아 일부 메서드를 구현하고 빈으로 등록하면된다.
  ```
  @Slf4j
  @Component
  public class ZuulLoggingFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException {
        log.info("========================== print zuul logs start ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info("========================== print zuul logs: " + request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
  } 
  ```
- 상황에 맞는 필터를 골라 `filterType`을 지정해주면 된다. 
- ```
  @Override
  public String filterType() {
    return "pre";
  }
  ```
- filterType 종류 
  - `pre` filter, `post` filter
    + 전처리, 후처리 필터 
  - `custom` filter
  - `routing` filter
    + 조건에 따라 특정 서버로 전달
  - `error` filter
- ```
    @Override
    public Object run() throws ZuulException {
        log.info("========================== print zuul logs start ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info("========================== print zuul logs: " + request.getRequestURI());
        return null;
    }
  ```
- `run()` 메서드에서 수행할 로직을 추가하면 된다.
- https://netflixtechblog.com/announcing-zuul-edge-service-in-the-cloud-ab3af5be08ee

## zuul 테스트 
![direct_api_call](/images/direct_api_call.png)
- 기존에 MemberController를 호출하기 위해 `localhost:8081/welcome`로 호출했었다. 

![zuul_proxy_call](/images/zuul_proxy_call.png)
- 이제는 직접 호출대신 gateway역할을 해주는 zuul을 통해서 호출할 수 있는데, `localhost:8000/member-service/welcome`로 호출하면 된다. 
- `application.yml`에 `route` 설정을 해줬듯이, `member-service/**`로 들어오는 url은 member-service 로 서빙해준다. 
- 호출시 마다 filter 로그도 찍힘
  + ```
    2022-01-24 22:53:35.302  INFO 81914 --- [nio-8000-exec-7] com.example.filter.ZuulLoggingFilter     : ========================== print zuul logs start 
    2022-01-24 22:53:35.303  INFO 81914 --- [nio-8000-exec-7] com.example.filter.ZuulLoggingFilter     : ========================== print zuul logs: /member/welcome
    ```

## 로드밸런싱 테스트 
![ribbon_loadbalance](/images/ribbon_loadbalance.png)
- MemberApplication을 복제하여 포트만 변경해서 구동시켜보자. 
- `curl -XGET http://localhost:8000/member-service/welcome` 으로 테스트해보면 라운드 로빈으로 호출되는 걸 테스트할 수 있다.  

# OpenFeign
- netflix에서 개발한 http client binder이다. 
- 단순하게 RestTemplate을 더 편리하게 사용할수 있도록 추상화해놓은.. 마치 Spring Data JPA와 같다고 생각하면 된다.
- 내부 소스가 어렵고 아직 관련 자료가 많지 않다. 

## OpenFeign 적용 
- gradle dependency 
  ```
  implementation('org.springframework.cloud:spring-cloud-starter-openfeign') 
  ```
- Application에 `@EnableFeignClients` 어노테이션 추가. 
  ```
  @SpringBootApplication
  @EnableEurekaClient
  @EnableFeignClients
  public class MemberApplication {
    public static void main(String[] args) {
      SpringApplication.run(MemberApplication.class, args);
    }
  } 
  ```
- feign client는 인터페이스로 만들어 간단하게 client api를 호출할 수 있다. 
  ```
  import org.springframework.cloud.openfeign.FeignClient;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;

  @FeignClient(name = "order-service")
  public interface OrderServiceClient {

    @GetMapping("{memberId}/orders")
    String getOrderByMemberId(@PathVariable String memberId);
  }
  ```
- zuul을 사용하고 있기 때문에 `name = "order-service"`만 붙여줘도 order-service를 호출해준다. 

## Feign 테스트 
- 기존 RestTemplate 호출을 v1로 두고, Feign-Client v2로 뒀다. 
- `curl -XGET http://localhost:8000/member-service/v2/2`로 호출해보면 정상 동작을 확인할 수 있다. 


## spring-cloud dependency 추가시 주의 사항 
![spring-cloud-version](/images/spring_cloud_version.png)
- `스프링 부트`에 맞는 `spring-cloud` 버전을 선택해야함. 
- https://spring.io/projects/spring-cloud



