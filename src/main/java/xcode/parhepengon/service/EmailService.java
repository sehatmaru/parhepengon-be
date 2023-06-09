package xcode.parhepengon.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import xcode.parhepengon.exception.AppException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("XCODE - PARHEPENGON | OTP Verification");
            String emailContent = "Your OTP: <b>" + otp + "</b>. This OTP is valid for 5 minutes.";
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(e.getMessage());
        }
    }

    public void sendResetPasswordEmail(String email, String code)  {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("XCODE - PARHEPENGON | Reset Password");
            String emailContent = "Click <a href=\"https://xcode-parhepengon.web.app/reset-password?code=" + code + "\">here</a> to reset your password. This link is valid for 5 minutes.";
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(e.getMessage());
        }
    }
}
