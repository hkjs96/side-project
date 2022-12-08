package com.sideproject.service;

import com.sideproject.dto.UserDTO;
import com.sideproject.entity.UserEntity;
import com.sideproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getByCredentials(UserDTO userDTO){
        // TODO: Profile은 어떻게 처리할까? 따로 API 개발??
        final UserEntity userEntity = userRepository.findById(userDTO.getId()).orElse(null);
        if (userEntity != null &&
            passwordEncoder.matches(
                    userDTO.getPassword(),
                    userEntity.getPassword())) {

            userDTO = UserDTO.builder()
                    .email(userEntity.getEmail())
                    .name(userEntity.getName())
                    .build();
            return userDTO;
        }
        return null;
    }

    public boolean getByEmail(UserDTO userDTO) {
        UserEntity userEntity = userRepository.findByEmail(userDTO.getEmail());
        return userEntity == null;
    }

    public boolean getById(final String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        return userEntity == null;
    }

    public UserDTO createUser(final UserDTO userDTO) {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .id(userDTO.getId())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
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
