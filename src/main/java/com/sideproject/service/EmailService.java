package com.sideproject.service;

import com.sideproject.dto.EmailDTO;

public interface EmailService {

    String sendSimpleMail(String recipient);

    Boolean verifyEmail(EmailDTO emailDTO);
}
