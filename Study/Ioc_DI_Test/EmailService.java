package Study.Ioc_DI_Test;

public class EmailService {
    public void sendEmail(String recipient, String message) {
        // 이메일 보내는 로직
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}