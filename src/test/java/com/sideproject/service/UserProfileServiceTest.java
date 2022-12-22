package com.sideproject.service;

import com.sideproject.dto.UserProfileDTO;
import com.sideproject.entity.User;
import com.sideproject.entity.UserProfile;
import com.sideproject.properties.StorageProperties;
import com.sideproject.repository.UserProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class UserProfileServiceTest {

    private UserProfileRepository userProfileRepository =
            Mockito.mock(UserProfileRepository.class);
    private FileSystemStorageService fileSystemStorageService =
            Mockito.mock(FileSystemStorageService.class);

    private UserProfileService userProfileService;

    @BeforeEach
    public void setUpTest() {
        userProfileService =
                new UserProfileService(userProfileRepository, fileSystemStorageService);
    }

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

        UserProfile savedUserProfile = userProfileService.createProfile(userProfileDto, user);

        assertEquals(savedUserProfile.getName(), userProfileDto.getName());
        assertEquals(savedUserProfile.getPhotoOriginalName(), userProfileDto.getPhotoName());
        assertEquals(savedUserProfile.getStudentId(), userProfileDto.getStudentId());

        verify(userProfileRepository).save(any());
    }

    @Test
    @DisplayName("프로필 이미지 저장하기")
    void 프로필_이미지_저장하기() {
        Mockito.when(fileSystemStorageService.store(
                any(MockMultipartFile.class),
                any(String.class)))
                .thenReturn("test.png");


        String fileName = userProfileService.saveProfileImage(new MockMultipartFile("foo", "../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()),
                UUID.randomUUID().toString());

        assertEquals(fileName.contains("png"), true);
    }

}