package com.sideproject.controller;

import com.sideproject.dto.EmailDTO;
import com.sideproject.dto.IdDTO;
import com.sideproject.dto.ResponseDTO;
import com.sideproject.dto.UserDTO;
import com.sideproject.entity.User;
import com.sideproject.security.TokenProvider;
import com.sideproject.service.EmailService;
import com.sideproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "아이디 중복 체크", notes = "사용할 아이디가 이미 존재하는지 확인.")
    @GetMapping ("/signup/{id}")
    public ResponseEntity<?> duplicateIdCheck(
            @ApiParam(name = "아이디", required = true) @PathVariable("id") String userId
    ) {

        boolean result = userService.getById(userId);

        if ( result ) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .data(new IdDTO(userId))
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                            .error("duplicated")
                            .data(new IdDTO(userId))
                            .build();
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(responseDTO);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(
            @ApiParam(name = "회원가입 정보", required = true) @RequestBody UserDTO userDTO
    ){

        try {
            // TODO: validation 체크하기, id 없으면 이상한 값으로 나간다.

            UserDTO userResponseDTO = userService.createUser(userDTO);

            if (userResponseDTO.getId() != null) {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .data(userDTO)
                        .build();

                return ResponseEntity.ok().body(responseDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("회원 가입 중 에러 발생 관리자에게 문의해주세요.")
                        .data(userDTO)
                        .build();

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("잘못된 요청입니다.")
                    .data(userDTO)
                    .build();

            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
           @ApiParam(name="로그인 정보") @RequestBody UserDTO userDTO
    ) {
         User user = userService.getByCredentials(userDTO);

        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .token(token)
                    .build();

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .data(responseUserDTO)
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .data(userDTO)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(responseDTO);
        }
    }

    @PostMapping ("/signup/email")
    public ResponseEntity<?> verifyEmail(
            @ApiParam(name = "이메일", readOnly = true) @RequestBody EmailDTO emailDTO
    ) {

        boolean verification = userService.getByEmail(emailDTO);

        try {
            if ( verification ) {
                emailService.sendSimpleMail(emailDTO.getEmail());

                ResponseDTO responseDTO = ResponseDTO.builder()
                        .data(emailDTO)
                        .build();

                return ResponseEntity.ok().body(responseDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("duplicated")
                        .data(emailDTO)
                        .build();
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(responseDTO);
            }
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("이메일 인증 중 에러 발생 관리자에게 문의 주세요.")
                    .data(emailDTO)
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
            @ApiParam(name = "이메일", readOnly = true) @RequestParam String email,
            @ApiParam(name = "인증번호", readOnly = true) @RequestParam String code
            ) {

        EmailDTO emailDTO = EmailDTO.builder().email(email).authenticationNumber(code).build();

        try {
            if(!emailService.verifyEmail(emailDTO)){
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("Email verification failure")
                        .data(emailDTO)
                        .build();

                return ResponseEntity
                        .status(409)
                        .body(responseDTO);
            }
            ResponseDTO responseDTO = ResponseDTO.builder()
//                    .error("Email verification successful")
                    .data(emailDTO)
                    .build();

            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("인증 코드 확인 중 에러 발생 관리자에게 문의 주세요.")
                    .data(emailDTO)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDTO);
        }
    }
}
