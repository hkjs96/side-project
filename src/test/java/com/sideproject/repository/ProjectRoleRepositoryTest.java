package com.sideproject.repository;

import com.sideproject.entity.Project;
import com.sideproject.entity.ProjectRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ProjectRoleRepositoryTest {

    @Autowired
    ProjectRoleRepository projectRoleRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void countByProjectId() {
        // given
        Project project = Project.builder()
                .name("테스트 프로젝트")
                .topic("테스트 프로젝트 주제")
                .goal("테스트 프로젝트 목표")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
        Project savedProject = projectRepository.save(project);

        ProjectRole projectRole = ProjectRole.builder()
                .name("기확자")
                .project(savedProject)
                .build();
        ProjectRole savedProjectRole = projectRoleRepository.save(projectRole);
        ProjectRole projectRole2 = ProjectRole.builder()
                .name("PM")
                .project(savedProject)
                .build();
        ProjectRole savedProjectRole2 = projectRoleRepository.save(projectRole2);

        // when
        Long count = projectRoleRepository.countByProjectId(savedProject.getId());

        // then
        assertEquals(2L, count);
    }
}