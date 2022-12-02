package com.sideproject.service;

public interface EmailService {

    String sendSimpleMail(String recipient);

    Boolean verifyEmail(String email, String authencatoinNumber);
}
