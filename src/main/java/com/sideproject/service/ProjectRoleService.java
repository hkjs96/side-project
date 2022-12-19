package com.sideproject.service;

import com.sideproject.dto.ProjectDTO;
import com.sideproject.dto.ProjectRoleDTO;
import com.sideproject.entity.Project;
import com.sideproject.entity.ProjectRole;
import com.sideproject.repository.ProjectRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectRoleService {

    private static ProjectRoleRepository projectRoleRepository;

    public ProjectRoleService(ProjectRoleRepository projectRoleRepository) {
        this.projectRoleRepository = projectRoleRepository;
    }

    @Transactional
    public List<ProjectRoleDTO> createProjectRole(ProjectDTO projectDTO, Project project) {

        for (ProjectRoleDTO projectRoleDTO : projectDTO.getProjectRoleDTOList()) {
            ProjectRole projectRole = ProjectRole.builder()
                    .project(project)
                    .name(projectRoleDTO.getName())
                    .build();

            projectRoleRepository.save(projectRole);
        }

        return projectDTO.getProjectRoleDTOList();
    }
}
