package com.sideproject.service;

import com.sideproject.constant.Authority;
import com.sideproject.entity.Project;
import com.sideproject.entity.ProjectRole;
import com.sideproject.entity.UserProfile;
import com.sideproject.entity.UserProfileProject;
import com.sideproject.repository.ProjectRoleRepository;
import com.sideproject.repository.UserProfileProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileProjectService {

    private UserProfileProjectRepository userProfileProjectRepository;
    private ProjectRoleService projectRoleService;

    @Autowired
    UserProfileProjectService(
            UserProfileProjectRepository userProfileProjectRepository,
            ProjectRoleService projectRoleService) {
        this.userProfileProjectRepository = userProfileProjectRepository;
        this.projectRoleService = projectRoleService;
    }

    public UserProfileProject createProfileProject(Project project, UserProfile userProfile, String roleName) {
        ProjectRole projectRole = projectRoleService.getProjectRole(roleName);

        UserProfileProject userProfileProject = UserProfileProject.builder()
                .project(project)
                .userProfile(userProfile)
                .authority(Authority.ALL)
                .projectRole(projectRole)
                .build();

        userProfileProjectRepository.save(userProfileProject);
        return userProfileProject;
    }
}
