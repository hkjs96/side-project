package com.sideproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateProjectDTO {
    private ProjectDTO project;
    private UserProfileDTO userProfile;
    private List<ProjectRoleDTO> projectRoles;
}