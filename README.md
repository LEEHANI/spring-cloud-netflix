
# Eureka
- 유레카 서버는 마이크로 서비스가 등록되거나 삭제될 때 자동으로 감지하는 역할을 수행
- Service Discovery 역할을 수행. 전화번호부 역할

## Eureka Server
[eureka_dashboard](/images/eureka_dashboard.png)
- 유레카 서버를 구동시키면 클라이언트들의 등록 상태를 확인하는 대시보드를 제공한다.
- 유레카 서버 이름은 `spring.application.name`에서 설정한다. 
- 유레카 서버는 다른 서버들보다 `먼저 실행`되어야 함.
- register-with-eureka 레지스트리에 자신을 등록할지 여부 
- fetch-registry 레지스트리에 있는 정보를 가져올지 여부 

## Eureka Client 
[eureka_add_client](/images/eureka_add_client.png)
- 유레카 클라이언트를 등록하면 대시보드에 클라이언드 정보가 추가된다. 
- defaultZone은 유레카 클라이언트가 접속할 주소 정보 
- prefer-ip-address는 호스트 이름 대신 ip 주소로 통신 


## spring-cloud dependency 추가시 주의 사항 
[spring-cloud-version](/images/spring_cloud_version.png)
- 스프링 부트에 맞는 spring-cloud 버전을 선택해야함. 
- https://spring.io/projects/spring-cloud
