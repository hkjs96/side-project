package com.sideproject.service;

import com.sideproject.entity.Project;
import com.sideproject.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ProjectServiceTest {

    // 모킹할 리포지토리 설정
    private ProjectRepository projectRepository =
            Mockito.mock(ProjectRepository.class);

    private ProjectRoleService projectRoleService =
            Mockito.mock(ProjectRoleService.class);;

    private UserProfileService userProfileService =
            Mockito.mock(UserProfileService.class);;

    private UserProfileProjectService userProfileProjectService =
            Mockito.mock(UserProfileProjectService.class);;

    // 모킹할 서비스
    private ProjectService projectService;

    @BeforeEach
    public void setUpTest() {
        // ProjectService 주입
        projectService = new ProjectService(
                projectRepository,
                projectRoleService,
                userProfileService,
                userProfileProjectService
        );
    }

    /*
        1. 프로젝트 이름, 프로젝트 종료 날짜 가져오기 (미완료 / 최신순으로 정렬하기)
        2. 프로젝트 진척도 확인하기 (체크 리스트 없을 경우 0% 처리하기)
        테스트 참고 : https://www.digitalocean.com/community/tutorials/mockito-argument-matchers-any-eq
    */
    @Test
    void 로그인_후_프로젝트_리스트_불러오기() {
        // any 배열, eq 하나
        Mockito.when(projectRepository.get(any(Project.class)));
    }
}