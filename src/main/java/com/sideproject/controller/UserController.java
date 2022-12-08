package com.sideproject.controller;

import com.sideproject.dto.EmailDTO;
import com.sideproject.dto.ResponseDTO;
import com.sideproject.dto.UserDTO;
import com.sideproject.entity.UserEntity;
import com.sideproject.security.TokenProvider;
import com.sideproject.service.EmailService;
import com.sideproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class UserController {
    // TODO: logger 설정 하기

    private final UserService userService;
    private final EmailService emailService;

    private final TokenProvider tokenProvider;

    @Autowired
    UserController(UserService userService, EmailService emailService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping ("/signup/id")
    public ResponseEntity<?> duplicateIdCheck(@RequestParam("id") String userId) {

        boolean result = userService.getById(userId);

        if ( result == true ) {
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

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){

        try {
            // TODO: validation 체크하기, id 없으면 이상한 값으로 나간다.

            UserDTO userResponseDTO = userService.createUser(userDTO);

            if (userResponseDTO.getId() != null) {
                return ResponseEntity.ok().body("success");
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("회원 가입 중 에러 발생 관리자에게 문의해주세요.")
                        .build();

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("잘못된 요청입니다.")
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
         UserEntity user = userService.getByCredentials(userDTO);

        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .token(token)
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

    @PostMapping ("/signup/email")
    public ResponseEntity<?> verifyEmail(@RequestBody UserDTO userDto) {

        boolean verification = userService.getByEmail(userDto);

        try {
            if ( verification == true ) {
                emailService.sendSimpleMail(userDto.getEmail());

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

    @GetMapping("/signup/email")
    public ResponseEntity<?> verifyAuthenticationNumber(
            @RequestParam String email,
            @RequestParam String code
            ) {

        try {
            if(!emailService.verifyEmail(email, code)){
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
