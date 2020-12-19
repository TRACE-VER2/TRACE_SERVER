package com.trace.traceproject.controller;

import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.service.MailService;
import com.trace.traceproject.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final ResponseService responseService;

    @GetMapping("/api/v1/mail/verification/{mail}")
    public SingleResult mailVerification(@PathVariable("mail") String mail) {
        Map<String, String> result = new HashMap<>();

        String verificationKey = mailService.sendValidationEmail(mail);
        result.put("verificationKey", verificationKey);

        return responseService.getSingleResult(result);
    }
}
