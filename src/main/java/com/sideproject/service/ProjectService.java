package com.sideproject.service;

import com.sideproject.dto.ProjectDTO;
import com.sideproject.entity.Project;
import com.sideproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private static ProjectRepository projectRepository;

    private static ProjectRoleService projectRoleService;

    @Autowired
    ProjectService(ProjectRepository projectRepository, ProjectRoleService projectRoleService) {
        this.projectRepository = projectRepository;
        this.projectRoleService = projectRoleService;
    }

    @Transactional
    public Long createProject(ProjectDTO projectDTO) {
        /*
            Project.save
            UserProject.save -> 사용자 추가가 아니라 프로젝트 생성자 프로필 추가
            ProjectRole.save
        */
        Project project = Project.builder()
                .name(projectDTO.getName())
                .topic(projectDTO.getTopic())
                .goal(projectDTO.getGoal())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .build();

        Project createdProject = projectRepository.save(project);

        projectRoleService.createProjectRole(projectDTO, createdProject);

        // TODO: 프로젝트 생성자 프로필 생성하기

        // TODO: 생성자 프로필과 프로젝트 관계에 나오는 Profile Project 입력하기

        return createdProject.getId();
    }
}
