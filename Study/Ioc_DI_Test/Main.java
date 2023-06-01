package Study.Ioc_DI_Test;

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