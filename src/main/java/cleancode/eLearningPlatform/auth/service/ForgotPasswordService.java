package cleancode.eLearningPlatform.auth.service;
import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ForgotPasswordService {

    public String generateForgotPasswordToken(User user, int length) {

        return generateRandomCode(length);
    }

    private String generateRandomCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
