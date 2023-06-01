Spring IoC & DI
===
Spring은 Java 기반의 오픈 소스 프레임워크로, 엔터프라이즈 애플리케이션 개발에 사용되는 대표적인 프레임워크

IoC (Inversion of Control, 제어의 역전)
---
> IoC는 개발자가 객체의 생성과 생명 주기 관리를 직접 처리하는 대신, 이를 IoC 컨테이너에게 위임하여 제어의 흐름을 바꾸는 개념

IoC 예제
-
```java
public class MyService {
    private MyRepository repository;

    // 의존성 주입을 통해 MyRepository 객체를 주입받음
    public MyService(MyRepository repository) {
        this.repository = repository;
    }

    public void doSomething() {
        // MyRepository 객체의 메서드를 사용
        repository.doAction();
    }
}

public class MyRepository {
    public void doAction() {
        // 데이터 액세스 로직 실행
        System.out.println("데이터 액세스 로직 실행");
    }
}

```
위 코드에서 MyService 클래스는 MyRepository 클래스에 의존성이 있습니다.  
그러나 MyService 클래스에서 MyRepository 객체를 직접 생성하는 것이 아니라 생성자를 통해 의존성을 주입받습니다.  
이렇게 의존성을 주입받는 것은 IoC의 한 예입니다. IoC 컨테이너가 MyService 객체를 생성할 때, MyRepository 객체를 생성하고 주입해줍니다.
<br>


DI (Dependency Injection, 의존성 주입)
---
>DI는 IoC의 구현 방식 중 하나로, 의존하는 객체를 직접 생성하지 않고 외부에서 주입하는 개념


DI 예제
-

```java
public class UserService {
    private EmailService emailService;

    public UserService(EmailService emailService) {
        // 의존성을 주입받음
        this.emailService = emailService;
    }

    public void registerUser(String username, String email) {
        // 사용자 등록 로직
        // ...

        // 이메일 전송
        emailService.sendEmail(email, "Welcome to our service!");
    }
}
```

```java
public class EmailService {
    public void sendEmail(String recipient, String message) {
        // 이메일 보내는 로직
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        // EmailService 객체 생성
        EmailService emailService = new EmailService();

        // UserService 객체 생성하면서 EmailService 객체 주입
        UserService userService = new UserService(emailService);

        // UserService의 메서드 호출
        userService.registerUser("john", "john@example.com");
    }
}

```
위 코드에서 UserService 클래스는 생성자를 통해 EmailService 객체를 주입받습니다.  
이렇게 의존성을 주입받으면 UserService 클래스는 어떤 EmailService 객체를 사용할지 신경 쓰지 않고, 외부에서 주입된 객체를 사용할 수 있습니다.  
Main 클래스에서 EmailService 객체를 생성하고, 이를 UserService 객체에 주입하여 사용하는 예시입니다.  
이렇게 하면 UserService 클래스는 EmailService 클래스에 대한 의존성을 알지 못하며, 유연성이 높아지고 테스트하기 쉬워집니다.  
다른 종류의 EmailService 구현체를 만들어서 주입하면, UserService 클래스의 코드를 수정하지 않고도 다른 이메일 서비스를 사용할 수 있습니다.


<br>

