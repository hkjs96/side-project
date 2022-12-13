package com.sideproject.controller;

import com.sideproject.config.WebMvcConfig;
import com.sideproject.config.WebSecurityConfig;
import com.sideproject.dto.ResponseDTO;
import com.sideproject.service.EmailService;
import com.sideproject.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ComponentScan(basePackages = "com/sideproject/security")
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    EmailService emailService;

    @Test
    @DisplayName("사용자 id 중복 테스트")
    void duplicateIdCheck() throws Exception {

        String userId = "user123";

        ResponseEntity responseEntity = ResponseEntity
                .ok().body("not duplicated");

        given(userService.getById(userId)).willReturn(true);

        mockMvc.perform(
                    get("/signup/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("not duplicated"))
                .andDo(print());

        verify(userService).getById(userId);
    }

    @Test
    @DisplayName("사용자 id 중복 테스트 실패")
    void failedDuplicateIdCheck() throws Exception {

        String userId = "user123";

        ResponseDTO responseDTO = ResponseDTO.builder()
                .error("duplicated")
                .build();
        ResponseEntity responseEntity = ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDTO);

        given(userService.getById(userId)).willReturn(false);

        mockMvc.perform(
                        get("/signup/" + userId)
                                .accept(MediaType.APPLICATION_JSON) )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("duplicated"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());

        verify(userService).getById(userId);
    }
}