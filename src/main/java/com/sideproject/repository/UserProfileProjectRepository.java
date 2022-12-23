package com.sideproject.repository;

import com.sideproject.entity.UserProfileProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileProjectRepository extends JpaRepository<UserProfileProject, Long> {

}
