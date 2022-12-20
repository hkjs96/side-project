package com.sideproject.service;

import com.sideproject.dto.UserProfileDTO;
import com.sideproject.entity.Project;
import com.sideproject.entity.User;
import com.sideproject.entity.UserProfile;
import com.sideproject.repository.UserProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class UserProfileServiceTest {

    private UserProfileRepository userProfileRepository =
            Mockito.mock(UserProfileRepository.class);

    private UserProfileService userProfileService;

    @BeforeEach
    public void setUpTest() { userProfileService = new UserProfileService(userProfileRepository); }

    @Test
    @DisplayName("프로젝트 생성자 프로필 입력하기")
    void 프로잭트_생성자_프로필_저장하기() {
        Mockito.when(userProfileRepository.save(any(UserProfile.class)))
                .then(returnsFirstArg()); // returnsFirstArg 첫번째 인수 반환

        User user = User.builder()
                .id("test")
                .email("test@test.com")
                .name("test")
                .build();

        UserProfileDTO userProfileDto = UserProfileDTO.builder()
                .name("프로젝트에 표시할 이름")
                .universal("테스트 대학교")
                .major("전공 과목")
                .studentId(202205102L)
                .explain("누구누구입니다~")
                .photoName("profile")
                .photoSize(3 * 1024L)
                .photoType(".png")
                .build();

        UserProfileDTO savedUserProfileDto = userProfileService.createProfile(userProfileDto, user);

        Assertions.assertEquals(savedUserProfileDto.getName(), userProfileDto.getName());
        Assertions.assertEquals(savedUserProfileDto.getPhotoName(), userProfileDto.getPhotoName());
        Assertions.assertEquals(savedUserProfileDto.getStudentId(), userProfileDto.getStudentId());

        verify(userProfileRepository).save(any());
    }

    @Test
    @DisplayName("프로필 이미지 저장하기")
    void 프로필_이미지_저장하기() {

    }

}