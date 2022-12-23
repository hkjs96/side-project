package com.sideproject.repository;

import com.sideproject.entity.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {

    Long countByProjectId(Long projectId);

    ProjectRole findByName(String roleName);
}
