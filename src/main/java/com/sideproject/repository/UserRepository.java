package com.sideproject.repository;

import com.sideproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByEmail(String email);
    UserEntity findByIdAndPassword(String id, String password);
}
