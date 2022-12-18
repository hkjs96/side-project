package com.sideproject.repository;

import com.sideproject.constant.Authority;
import com.sideproject.entity.Participation;
import com.sideproject.entity.Project;
import com.sideproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ParticipationRepositoryTest {
    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void saveTest() {
        // given
        User user =  User.builder()
                .id("test")
                .password("test")
                .email("test@test.com")
                .name("홍길동")
                .build();
        User savedUser = userRepository.save(user);

        Project project = Project.builder()
                .name("테스트 프로젝트")
                .topic("테스트 프로젝트 주제")
                .description("테스트 프로젝트 설명")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
        Project savedProject = projectRepository.save(project);

        Participation participation = Participation.builder()
                .user(user)
                .authority(Authority.ALL)
                .project(project)
                .build();

        // when
        Participation savedParticipation = participationRepository.save(participation);
        assertEquals(savedUser.getId(), savedParticipation.getUser().getId());
        assertEquals(savedProject.getId(), savedParticipation.getProject().getId());
        assertEquals(participation.getAuthority(), savedParticipation.getAuthority());
    }
}