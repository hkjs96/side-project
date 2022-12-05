package com.sideproject.service;

import com.sideproject.dto.UserDTO;
import com.sideproject.entity.UserEntity;
import com.sideproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean getById(final String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        System.err.println(userEntity);
        return userEntity == null ? true : false;
    }

    public UserDTO createUser(final UserDTO userDTO) {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .id(userDTO.getId())
                    .password(userDTO.getPassword())
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .terms(userDTO.getTerms())
                    .build();

            UserEntity createUser = userRepository.save(userEntity);

            createUser = userRepository.findById(createUser.getId()).get();

            UserDTO createUserDTO = UserDTO.builder()
                    .id(createUser.getId())
                    .name(createUser.getName())
                    .email(createUser.getEmail())
                    .terms(createUser.getTerms())
                    .build();

            return createUserDTO;
        } catch (Exception e) {
            // TODO: log 달기
            return new UserDTO();
        }

    }
}
