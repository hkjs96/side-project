package com.sideproject.dto;

import lombok.Data;

@Data
public class CreateProjectDTO {
    private ProjectDTO project;
    private UserProfileDTO userProfile;
    private ProjectRoleDTO projectRole;
}