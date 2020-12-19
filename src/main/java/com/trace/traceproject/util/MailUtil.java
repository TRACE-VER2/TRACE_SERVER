package com.trace.traceproject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@RequiredArgsConstructor
@Component
public class MailUtil {
    private final JavaMailSender javaMailSender;

    /**
     * 첨부파일 없이 이메일 보내기
     * 첨부파일 넣으려면 MIME 형식으로 보내야함
     */
    private void sendEmail(String mailAddress, String title, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailAddress);
        mail.setSubject(title);
        mail.setText(message);
        javaMailSender.send(mail);
    }

    //가입 인증 이메일 전송
    public String sendValidationEmail(String mailAddress) {
        //6자리 인증 번호 발급
        String tmpKey = getRandomKey(6);
        String title = "트레이스 이메일 인증 번호입니다.";
        String message = "안녕하세요. 아래의 인증 번호를 입력하고 가입을 완료해주세요.\n" +
                "\n\n 인증 번호 : " +
                tmpKey;

        sendEmail(mailAddress, title, message);

        return tmpKey;
    }

    //임시 비밀번호 이메일 전송
    public String sendPasswordChangeEmail(String mailAddress, String name) {
        //임시 비밀번호 발급
        String tmpPassword = getRandomPassword();
        String title = name + "님의 트레이스 임시 비밀번호 안내 이메일입니다.";
        String message = "안녕하세요. 임시 비밀번호 안내 관련 이메일입니다. \n" +
                "임시 비밀번호는 " + tmpPassword + " 입니다.\n" +
                "로그인 후 비밀번호를 꼭 변경하여주시기 바랍니다.";

        sendEmail(mailAddress, title, message);

        return tmpPassword;
    }

    //랜덤 인증 키 발급
    private String getRandomKey(int size) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num;

        while (buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
    }

    //임시 비밀번호 발급
    private String getRandomPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        StringBuffer buffer = new StringBuffer();

        int idx;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            buffer.append(charSet[idx]);
        }
        return buffer.toString();
    }
}
