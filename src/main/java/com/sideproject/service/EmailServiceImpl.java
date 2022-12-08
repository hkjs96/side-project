package com.sideproject.service;

import com.sideproject.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;

    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}") private String sender;

    // TODO: https://www.baeldung.com/spring-email
    // TODO: 비동기 요청으로 변환할 수 있는가?
    @Override
    @Transactional
    public String sendSimpleMail(String recipient) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            String authenticationNumber;

            // 네이버에서는 FROM 이 설정한 이메일이 아니라면 거부당한다.
            mailMessage.setFrom(sender + "@gmail.com");
            mailMessage.setTo(recipient);
            authenticationNumber = generateAuthenticationNumber();
            mailMessage.setText("인증 번호 : " + authenticationNumber);
            // TODO: 메시지 어떻게 담을 지 알아야 함.
            mailMessage.setSubject("이메일 인증");

            javaMailSender.send(mailMessage);

            // 3 분
            redisUtil.setDataExpire(recipient, authenticationNumber, 60 * 3L);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            throw new RuntimeException("Error while Sending Mail");
        }
    }

    @Override
    public Boolean verifyEmail(String email, String authenticationNumber) {
        try {
            if (authenticationNumber.equals(redisUtil.getData(email))) {
                redisUtil.delete(email);
                return true;
            } else {
//               throw new RuntimeException("Invalid request");
               return false;
            }
        } catch(Exception e) {
            throw new RuntimeException("Error while Verifying Mail");
        }
    }

    private String generateAuthenticationNumber() {
        SecureRandom secureRandom = new SecureRandom();
        int max = 999999;
        int min = 111111;
        return String.valueOf(secureRandom.nextInt(min, max));
    }


}
