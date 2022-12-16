package com.sideproject.service;

import com.sideproject.dto.EmailDTO;
import com.sideproject.dto.UserDTO;
import com.sideproject.entity.User;
import com.sideproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User getByCredentials(UserDTO userDTO){
        // TODO: Profile은 어떻게 처리할까? 따로 API 개발??
        final User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null  &&
            passwordEncoder.matches(
                    userDTO.getPassword(),
                    user.getPassword())) {

            return user;
        }
        return null;
    }

    public boolean getByEmail(EmailDTO emailDTO) {
        User user = userRepository.findByEmail(emailDTO.getEmail());
        return user == null;
    }

    public boolean getById(final String userId) {
        User user = userRepository.findById(userId).orElse(null);

        return user == null;

    }

    public UserDTO createUser(final UserDTO userDTO) {
        try {
            if(userDTO.getId() == null) new RuntimeException("Invalid arguments");
            if(userRepository.existsById(userDTO.getId())) {
                // TODO: log 추가하기
//                log.warn("Username already exists {]", userDTO.getId());
                throw new RuntimeException("Username already exists");
            }
            User user = User.builder()
                    .id(userDTO.getId())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .build();

            User createUser = userRepository.save(user);

            createUser = userRepository.findById(createUser.getId()).get();

            UserDTO createUserDTO = UserDTO.builder()
                    .id(createUser.getId())
                    .name(createUser.getName())
                    .email(createUser.getEmail())
                    .build();

            return createUserDTO;
        } catch (Exception e) {
            // TODO: log 달기
            return new UserDTO();
        }

    }
}
