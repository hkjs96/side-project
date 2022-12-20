package com.sideproject.repository;

import com.sideproject.constant.Authority;
import com.sideproject.entity.UserProfile;
import com.sideproject.entity.UserProfileProject;
import com.sideproject.entity.Project;
import com.sideproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


//@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserProfileProjectRepositoryTest {
    @Autowired
    UserProfileProjectRepository userProfileProjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Test
    void saveTest() {
        // given
        User user =  User.builder()
                .id("test")
                .password("test")
                .email("test@test.com")
                .name("홍길동")
                .build();
        User savedUser = userRepository.save(user);

        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .explain("프로필 설명")
                .major("전공")
                .name("프로필 표시 이름")
                .studentId(201705134L)
                .universal("테스트 대학")
                .photoName("testImage")
                .photoSize(3 * 1024L) // 3 * 1024 * 1Byte  => 3 MB 크기의 이미지
                .photoType(".png")
                .build();
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        Project project = Project.builder()
                .name("테스트 프로젝트")
                .topic("테스트 프로젝트 주제")
                .goal("테스트 프로젝트 목표")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
        Project savedProject = projectRepository.save(project);

        UserProfileProject userProfileProject = UserProfileProject.builder()
                .userProfile(savedUserProfile)
                .authority(Authority.ALL)
                .project(project)
                .build();

        // when
        UserProfileProject savedUserProfileProject = userProfileProjectRepository.save(userProfileProject);

        // then
        assertEquals(savedUser.getId(), savedUserProfileProject.getUserProfile().getUser().getId());
        assertEquals(savedProject.getId(), savedUserProfileProject.getProject().getId());
        assertEquals(userProfileProject.getAuthority(), savedUserProfileProject.getAuthority());
    }
}