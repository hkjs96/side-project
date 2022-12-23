package com.sideproject.service;

import com.sideproject.dto.UserProfileDTO;
import com.sideproject.entity.Project;
import com.sideproject.entity.ProjectRole;
import com.sideproject.entity.UserProfile;
import com.sideproject.entity.UserProfileProject;
import com.sideproject.repository.ProjectRoleRepository;
import com.sideproject.repository.UserProfileProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

class UserProfileProjectServiceTest {

    private UserProfileProjectRepository userProfileProjectRepository
            = Mockito.mock(UserProfileProjectRepository.class);

    private ProjectRoleService projectRoleService
            = Mockito.mock(ProjectRoleService.class);

    private ProjectRoleRepository projectRoleRepository
            = Mockito.mock(ProjectRoleRepository.class);

    private UserProfileProjectService userProfileProjectService;

    @BeforeEach
    public void setUpTest() {
        userProfileProjectService =
                new UserProfileProjectService(userProfileProjectRepository, projectRoleService); }


    @Test
    void 프로젝트_사용자간의_관계_생성하기() {
        UserProfileProject userProfileProject = UserProfileProject.builder()
                        .projectRole(ProjectRole.builder()
                                .name("기획").build()).build();
        Mockito.when(userProfileProjectRepository.save(any(UserProfileProject.class)))
                .thenReturn(userProfileProject);

        Project project = Project.builder()
                .id(1L)
                .name("테스트 프로젝트")
                .topic("주제1")
                .goal("목표1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1L))
                .build();

        UserProfile userProfile = UserProfile.builder()
                .name("프로젝트에 표시할 이름")
                .universal("테스트 대학교")
                .major("전공 과목")
                .studentId(202205102L)
                .explain("누구누구입니다~")
                .photoName("profile")
                .photoSize(3 * 1024L)
                .photoType(".png")
                .build();

        String roleName = "기획";

        UserProfileProject createProfileProject = userProfileProjectService.createProfileProject(project, userProfile, roleName);

    }

}