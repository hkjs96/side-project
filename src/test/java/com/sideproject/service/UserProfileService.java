package com.sideproject.service;

import com.sideproject.dto.UserProfileDTO;
import com.sideproject.entity.User;
import com.sideproject.entity.UserProfile;
import com.sideproject.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private static UserProfileRepository userProfileRepository;

    @Autowired
    UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfileDTO createProfile(UserProfileDTO userProfileDto, User user) {
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .name(userProfileDto.getName())
                .universal(userProfileDto.getUniversal())
                .studentId(userProfileDto.getStudentId())
                .major(userProfileDto.getMajor())
                .explain(userProfileDto.getExplain())
                .photoName(userProfileDto.getPhotoName())
                .photoType(userProfileDto.getPhotoType())
                .photoSize(userProfileDto.getPhotoSize())
                .build();

        userProfileRepository.save(userProfile);

        return userProfileDto;
    }
}
