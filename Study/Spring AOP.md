Spring AOP
===
Spring은 Java 기반의 오픈 소스 프레임워크로, 엔터프라이즈 애플리케이션 개발에 사용되는 대표적인 프레임워크

AOP (Aspect Oriented Programming, 관점 지향 프로그래밍)
---
> AOP는 애플리케이션의 핵심 비즈니스 로직에서 공통적으로 사용되는 부가 기능을 모듈화하여 관리하는 방법

<br>

AOP 주요 개념
---
- Aspect : 위에서 설명한 흩어진 관심사를 모듈화 한 것. 주로 부가기능을 모듈화함.  
- Target : Aspect를 적용하는 곳 (클래스, 메서드 .. )  
- Advice : 실질적으로 어떤 일을 해야할 지에 대한 것, 실질적인 부가기능을 담은 구현체  
- JointPoint : Advice가 적용될 위치, 끼어들 수 있는 지점. 메서드 진입 지점, 생성자 호출 시점, 필드에서 값을 꺼내올 때 등 다양한 시점에 적용가능  
- PointCut : JointPoint의 상세한 스펙을 정의한 것. 'A란 메서드의 진입 시점에 호출할 것'과 같이 더욱 구체적으로 Advice가 실행될 지점을 정할 수 있음

<br>

AOP 특징
---
- 프록시 패턴 기반의 AOP 구현체, 프록시 객체를 쓰는 이유는 접근 제어 및 부가기능을 추가하기 위해서임  
- 스프링 빈에만 AOP를 적용 가능  
- 모든 AOP 기능을 제공하는 것이 아닌 스프링 IoC와 연동하여 엔터프라이즈 애플리케이션에서 가장 흔한 문제(중복코드, 프록시 클래스 작성의 번거로움, 객체들 간 관계 복잡도 증가 ...)에 대한 해결책을 지원하는 것이 목적  

<br>

AOP 예제

의존성 추가
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
비즈니스 로직
```java
@Service
public class MyService {
    public void doSomething() {
        // 비즈니스 로직 실행
        System.out.println("실제 비즈니스 로직이 실행됩니다.");
    }
}
```
Aspect로 사용될 클래스를 생성
```java
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.example.MyService.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature() + " 메소드 실행 시간: " + (endTime - startTime) + "ms");
        return result;
    }
}
```
메인 클래스 생성
```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);

        MyService service = applicationContext.getBean(MyService.class);
        service.doSomething();
    }
}
```
MyApplication 클래스에서는 Spring Boot 애플리케이션을 실행하고, applicationContext.getBean(MyService.class)를 통해 Spring IoC 컨테이너에서 MyService 클래스의 빈을 가져와서 doSomething 메소드를 호출.  
이때 AOP 설정에 의해 logExecutionTime Advice가 적용되어 실행 시간이 로그로 출력.


