package Study.Ioc_DI_Test;

import Study.Ioc_DI_Test.EmailService;

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