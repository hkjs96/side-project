package com.sideproject.service;

import com.sideproject.dto.ProjectDTO;
import com.sideproject.dto.ProjectRoleDTO;
import com.sideproject.entity.Project;
import com.sideproject.entity.ProjectRole;
import com.sideproject.repository.ProjectRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ProjectRoleServiceTest {

    private ProjectRoleRepository projectRoleRepository =
            Mockito.mock(ProjectRoleRepository.class);
    private ProjectRoleService projectRoleService;

    @BeforeEach
    public void setUpTest() {
        projectRoleService = new ProjectRoleService(projectRoleRepository);
    }


    @Test
    void createProjectRoleTest() {
        Mockito.when(projectRoleRepository.save(any(ProjectRole.class)))
                .then(returnsFirstArg()); // returnsFirstArg() 첫번째 인수 반환

        Project project = Project.builder()
                .id(1L)
                .name("테스트 프로젝트")
                .topic("주제1")
                .goal("목표1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1L))
                .build();

        ProjectRoleDTO projectRoleDto = new ProjectRoleDTO("기획자");

        ProjectDTO projectDTO = ProjectDTO.builder()
                        .ProjectRoleDTOList(
                                Arrays.asList(projectRoleDto))
                        .build();

        List<ProjectRoleDTO> projectRoleDtos = projectRoleService.createProjectRole(projectDTO, project);

        Assertions.assertEquals(projectRoleDtos.get(0).getName(), "기획자");

        verify(projectRoleRepository).save(any());
    }
}