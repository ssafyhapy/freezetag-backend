package com.ssafy.freezetag.global.util.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.ssafy.freezetag.domain.common.constant.MailConstant.MEMORYBOX_MAIL_TARGET_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final ResourceLoader resourceLoader;

    public void sendEmail(String toEmail, String title, String text) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        try {
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(text, true); // HTML 내용
            emailSender.send(message);
        } catch (MessagingException e) {
            log.debug("MailService.sendEmail 예외 발생 toEmail: {}, title: {}, text: {}", toEmail, title, text);
            throw new Exception("이메일 전송에 실패했습니다.");
        }
    }

    public String buildVerificationEmail() {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/memorybox-email.html");
            String content = Files.readString(Path.of(resource.getURI()), StandardCharsets.UTF_8);
            return content.replace("{{YOUR_URL_HERE}}", MEMORYBOX_MAIL_TARGET_URL);
        } catch (Exception e) {
            log.error("이메일 템플릿 로딩 실패", e);
            throw new RuntimeException("이메일 템플릿 로딩에 실패했습니다.");
        }
    }
}
