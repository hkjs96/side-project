package com.sideproject.controller;

import com.sideproject.dto.EmailDTO;
import com.sideproject.dto.ResponseDTO;
import com.sideproject.dto.UserDTO;
import com.sideproject.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class UserController {
    // TODO: logger 설정 하기

    private final EmailService emailService;

    UserController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping ("/user/duplicate")
    public ResponseEntity<?> duplicateIdCheck(@RequestParam("id") String userId) {
//        String encodedUserId = Base64.getEncoder().encodeToString(userId.getBytes());

        String decodedUserId = decodeBase64(userId);

        // TODO: DB에 중복된 값이 있는 지 조회
        UserDTO user = UserDTO.builder()
                .id(decodedUserId)
                .build();
        if ( user == null ) {
            return ResponseEntity.ok().body("not duplicated");
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                            .error("duplicated")
                            .build();
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(responseDTO);
        }
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        // UserEntity user =

//        if (user != null) {

        // TODO: 변경하기 userEntity 가 조회 되나 안되나로
//        userDTO.setId(null);
        if (userDTO.getId() != null) {
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username("임시 유저")
                    .id(userDTO.getId())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping ("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody UserDTO userDto) {
        String decodedEmail = decodeBase64(userDto.getEmail());

        // TODO: DB에 중복된 값이 있는 지 조회
        UserDTO user = UserDTO.builder()
                .email(decodedEmail)
                .build();
        try {
            if ( user != null ) {
                emailService.sendSimpleMail(decodedEmail);

                return ResponseEntity.ok().body("not duplicated");
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("duplicated")
                        .build();
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("이메일 인증 중 에러 발생 관리자에게 문의 주세요.")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        }
    }

    private String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    @PostMapping("/email/authentication")
    public ResponseEntity<?> verifyAuthenticationNumber(
            @RequestBody EmailDTO emailDTO
            ) {
        String decodedEmail = decodeBase64(emailDTO.getEmail());
        String decodedAuthenticationNumber = decodeBase64(emailDTO.getAuthenticationNumber());

        try {
            if(!emailService.verifyEmail(decodedEmail, decodedAuthenticationNumber)){
                return ResponseEntity
                        .status(409)
                        .body("Email verification failure");
            };
            return ResponseEntity
                    .ok()
                    .body("Email verification successful");
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("인증 코드 확인 중 에러 발생 관리자에게 문의 주세요.")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        }
    }
}
