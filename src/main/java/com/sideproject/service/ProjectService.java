package com.sideproject.service;

import com.sideproject.dto.CreateProjectDTO;
import com.sideproject.entity.Project;
import com.sideproject.entity.User;
import com.sideproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProjectService {

    private static ProjectRepository projectRepository;

    private static ProjectRoleService projectRoleService;

    private static UserProfileService userProfileService;

    @Autowired
    ProjectService(
            ProjectRepository projectRepository,
            ProjectRoleService projectRoleService,
            UserProfileService userProfileService
            ) {
        this.projectRepository = projectRepository;
        this.projectRoleService = projectRoleService;
        this.userProfileService = userProfileService;
    }

    @Transactional
    public Long createProject(CreateProjectDTO createProjectDTO, MultipartFile file, User user) {
        /*
            Project.save
            UserProject.save -> 사용자 추가가 아니라 프로젝트 생성자 프로필 추가
            ProjectRole.save
        */
        Project project = Project.builder()
                .name(createProjectDTO.getProject().getName())
                .topic(createProjectDTO.getProject().getTopic())
                .goal(createProjectDTO.getProject().getGoal())
                .startDate(createProjectDTO.getProject().getStartDate())
                .endDate(createProjectDTO.getProject().getEndDate())
                .build();

        Project createdProject = projectRepository.save(project);

        projectRoleService.createProjectRole(createProjectDTO, createdProject);

        // TODO: 프로젝트 생성자 프로필 생성하기, controller에서 UserProfileDTO에서 파일 내용 담아서 오기
        userProfileService.createProfile(createProjectDTO.getUserProfile(), user);

//        userProfileService.saveProfileImage();


        // TODO: 생성자 프로필과 프로젝트 관계에 나오는 Profile Project 입력하기

        return createdProject.getId();
    }
}
